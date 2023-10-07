package site.chagok.server.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;
import site.chagok.server.common.exception.AuthorizationApiException;

import javax.net.ssl.SSLException;
import java.time.Duration;

@Slf4j
@Service
public class OAuthService {

    /*

        프론트측, OAuth 로그인 방식 기반, 소셜 로그인, 로그아웃 서비스

        로그인 과정
        1. 액세스 토큰으로 인증서버에서 회원 정보 얻어옴
        2. DB에 없으면 회원가입
        3. 헤더에 JWT 토큰 발급
     */

    private WebClient webClient = getProxyEnableWebClient();

    // 구글 액세스 토큰 주소
    private final String googleAccessTokenUri = "https://openidconnect.googleapis.com/v1/userinfo";

    // 카카오 access 토큰 주소
    private final String kakaoAccessTokenUri = "https://kapi.kakao.com/v2/user/me";

    public OAuthService() throws SSLException {
    }

    public WebClient getProxyEnableWebClient(){
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(20000))
                .proxyWithSystemProperties();

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    // 소셜 로그인 사용자로부터 이메일 획득
    public String getGoogleCredential(String accessToken) {

        // access token으로 사용자 정보 획득
        UriComponentsBuilder googleUriBuilder = UriComponentsBuilder
                .fromUriString(googleAccessTokenUri)
                .queryParam("access_token", accessToken);

        String userJsonStr = webClient.get()
                .uri(googleUriBuilder.toUriString())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> { // invalid token 에러시,
                    if (response.statusCode().is4xxClientError())
                        throw new AuthorizationApiException("auth_01", "invalid google access token");
                    return null;
                })
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode userMap = null;
        try {
            userMap = objectMapper.readTree(userJsonStr);
        } catch (JsonProcessingException e) {
            throw new AuthorizationApiException("auth_01", "authorization process error");
        }

        return userMap.get("email").asText();
    }

    public String getKakaoResponse(String accessToken) {
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
                        throw new AuthorizationApiException("auth_01", "invalid kakao access token");
                    return null;
                })
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode userJson = null;
        try {
            userJson = objectMapper.readTree(userJsonStr);
        } catch (JsonProcessingException e) {
            throw new AuthorizationApiException("auth_01", "authorization process error");
        }

        // 사용자 이메일 반환
        return userJson.get("kakao_account").get("email").asText();
    }

}
