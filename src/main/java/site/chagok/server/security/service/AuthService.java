package site.chagok.server.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import site.chagok.server.common.contstans.SocialType;
import site.chagok.server.member.service.MemberLoggingService;
import site.chagok.server.security.dto.SignInRequestDto;
import site.chagok.server.security.dto.SignInResponseDto;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    /*
    프론트측, OAuth 로그인 방식 기반, 소셜 로그인, 로그아웃 서비스
     */

    private final MemberLoggingService memberLoggingService;
    private final JWTTokenService jwtTokenService;

    @Value("${front.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${front.kakao.client-id}")
    private String kakaoClientId;

    private WebClient webClient = WebClient.create();

    // 구글 액세스 토큰 주소
    private final String googleAccessTokenUri = "https://openidconnect.googleapis.com/v1/userinfo";
    // 카카오 authrization 토큰 주소
    private final String kakaoAuthTokenUri = "https://kauth.kakao.com/oauth/token";
    private final String kakaoAccessTokenUri = "https://kapi.kakao.com/v2/user/me";

    // 로그인 서비스
    /*
        로그인 과정
        1. 액세스 토큰으로 인증서버에서 회원 정보 얻어옴
        2. DB에 없으면 회원가입
        3. 헤더에 JWT 토큰 발급
     */
    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) throws JsonProcessingException {

        String userEmail = null;
        // userEmail 획득
        if (signInRequestDto.getSocialType() == SocialType.Google)
            userEmail = getGoogleCredential(signInRequestDto.getAccessToken());
        else if (signInRequestDto.getSocialType() == SocialType.Kakao)
            userEmail = getKakaoResponse(signInRequestDto.getAuthorizationToken());
        else
            throw new RuntimeException("social type error");

        // 회원가입 유무
        boolean isSignUp = memberLoggingService.signUp(userEmail, signInRequestDto.getSocialType());

        // jwt토큰 사용자 이메일, 권한
        String jwtToken = jwtTokenService.constructJWTToken(userEmail, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        return SignInResponseDto.builder()
                .signUp(isSignUp)
                .jwtToken(jwtToken)
                .refreshToken("12345")
                .build();
    }

    // 소셜 로그인 사용자로부터 이메일 획득
    private String getGoogleCredential(String accessToken) throws JsonProcessingException {

        // access token으로 사용자 정보 획득
        UriComponentsBuilder googleUriBuilder = UriComponentsBuilder
                .fromUriString(googleAccessTokenUri)
                .queryParam("access_token", accessToken);

        String userJsonStr = webClient.get()
                .uri(googleUriBuilder.toUriString())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> { // invalid token 에러시,
                    if (response.statusCode().is4xxClientError())
                        throw new AuthorizationServiceException("invalid access code");
                    return null;
                })
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode userMap = objectMapper.readTree(userJsonStr);

        return userMap.get("email").asText();
    }

    private String getKakaoResponse(String authorizationToken) throws JsonProcessingException {
        /*
            authorizationToken 전달받고,
            카카오 authorization server에서 액세스 토큰 획득 이후,
            사용자 정보 획득.
         */


        // authorization token으로 access token 획득
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("grant_type", "authorization_code");
        bodyMap.add("client_id", kakaoClientId);
        bodyMap.add("redirect_uri", kakaoRedirectUri);
        bodyMap.add("code", authorizationToken);

        String authTokenJsonStr = webClient.post()
                                .uri(kakaoAuthTokenUri)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(BodyInserters.fromFormData(bodyMap))
                                .retrieve()
                                .onStatus(HttpStatus::is4xxClientError, response -> { // invalid token 에러시,
                                    if (response.statusCode().is4xxClientError())
                                        throw new AuthorizationServiceException("invalid authorization code");
                                    return null;
                                })
                                .bodyToMono(String.class)
                                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode authJson = objectMapper.readTree(authTokenJsonStr);
        String accessToken = authJson.get("access_token").asText();

        // access token 으로 사용자 정보 획득
        String userJsonStr = webClient.post()
                .uri(kakaoAccessTokenUri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> { // invalid token 에러시,
                    if (response.statusCode().is4xxClientError())
                        throw new AuthorizationServiceException("invalid access code");
                    return null;
                })
                .bodyToMono(String.class)
                .block();

        JsonNode userJson = objectMapper.readTree(userJsonStr);

        // 사용자 이메일 반환
        return userJson.get("kakao_account").get("email").asText();
    }
}
