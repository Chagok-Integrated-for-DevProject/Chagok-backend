package site.chagok.server.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OAuthService {

    /*

        프론트측, OAuth 로그인 방식 기반, 소셜 로그인, 로그아웃 서비스

        로그인 과정
        1. 액세스 토큰으로 인증서버에서 회원 정보 얻어옴
        2. DB에 없으면 회원가입
        3. 헤더에 JWT 토큰 발급
     */

    private WebClient webClient = WebClient.create();

    // 구글 액세스 토큰 주소
    private final String googleAccessTokenUri = "https://openidconnect.googleapis.com/v1/userinfo";

    // 카카오 access 토큰 주소
    private final String kakaoAccessTokenUri = "https://kapi.kakao.com/v2/user/me";


    // 소셜 로그인 사용자로부터 이메일 획득
    public String getGoogleCredential(String accessToken) throws JsonProcessingException {

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

    public String getKakaoResponse(String accessToken) throws JsonProcessingException {
        /*
            authorizationToken 전달받고,
            카카오 authorization server에서 액세스 토큰 획득 이후,
            사용자 정보 획득.
         */

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

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode userJson = objectMapper.readTree(userJsonStr);

        // 사용자 이메일 반환
        return userJson.get("kakao_account").get("email").asText();
    }
}
