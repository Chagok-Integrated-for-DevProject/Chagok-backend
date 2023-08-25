package site.chagok.server.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import site.chagok.server.common.contstans.SocialType;
import site.chagok.server.member.service.MemberLoggingService;
import site.chagok.server.security.dto.SignInRequestDto;
import site.chagok.server.security.dto.SignInResponseDto;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    /*
    프론트측, OAuth 로그인 방식 기반, 소셜 로그인, 로그아웃 서비스
     */

    private final MemberLoggingService memberLoggingService;
    private final JWTTokenService jwtTokenService;

    private final String googleAccessTokenUrl = "https://openidconnect.googleapis.com";
    private final String googleAccessParam = "access_token";

    private WebClient googleWebClient;
    private WebClient kakaoWebClient;


    @PostConstruct
    public void init() {
        googleWebClient = WebClient.builder().baseUrl(googleAccessTokenUrl).build();
    }


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
            userEmail = getKakaoResponse(signInRequestDto.getAccessToken());
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

        String userJsonStr = googleWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/userinfo")
                        .queryParam(googleAccessParam, accessToken)  // access token 파라미터 추가
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    if (response.statusCode().is4xxClientError())
                        throw new AuthorizationServiceException("invalid access code");
                    return null;
                })
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> userMap = objectMapper.readValue(userJsonStr, new TypeReference<Map<String, String>>() {});

        return userMap.get("email");
    }

    private String getKakaoResponse(String accessToken) {
        return null;
    }
}
