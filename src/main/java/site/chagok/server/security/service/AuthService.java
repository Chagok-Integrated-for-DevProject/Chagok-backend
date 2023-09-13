package site.chagok.server.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import site.chagok.server.common.contstans.SocialType;
import site.chagok.server.security.dto.JwtTokenSetDto;
import site.chagok.server.security.dto.ReqSignInDto;
import site.chagok.server.security.domain.AuthInfo;
import site.chagok.server.security.dto.SignUpDto;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    /*
        사용자 회원가입, 로그인, 토큰 발급 관리 서비스
    */

    private final AccountService accountService;
    private final JWTTokenService jwtTokenService;
    private final OAuthService oAuthService;


    // 사용자 로그인
    public AuthInfo signIn(ReqSignInDto reqSignInDto) {

        String userEmail = getUserEmail(reqSignInDto.getAccessToken(), reqSignInDto.getSocialType());

        // 회원가입 유무
        boolean isSignUp = accountService.isSignUp(userEmail);

        if (!isSignUp) // 가입이 안되어 있다면,
            return new AuthInfo(false);

        // jwt토큰 사용자 이메일, 권한
        return jwtTokenService.issueJWTToken(userEmail, List.of("ROLE_USER"));
    }

    // refresh 토큰 재발급
    public AuthInfo refresh(JwtTokenSetDto jwtTokenSetDto)  {

        return jwtTokenService.renewRefreshToken(jwtTokenSetDto);
    }

    // 사용자 회원가입
    public AuthInfo signUp(SignUpDto signUpDto){

        String userEmail = getUserEmail(signUpDto.getAccessToken(), signUpDto.getSocialType());

        accountService.signUp(userEmail, signUpDto);

        // 회원가입 성공 후 jwt 토큰 발급
        return jwtTokenService.issueJWTToken(userEmail, List.of("ROLE_USER"));
    }

    // 사용자 user email 얻어오기
    private String getUserEmail(String accessToken, SocialType socialType){
        disableSslVerification();

         try {
             switch (socialType) {
                 case Google:
                     return oAuthService.getGoogleCredential(accessToken);
                 case Kakao:
                     return oAuthService.getKakaoResponse(accessToken);
                 default:
                     throw new AuthorizationServiceException("social type error");
             }
         } catch (JsonProcessingException e) {
             throw new AuthorizationServiceException("cannot get user data");
         }
    }
    private void disableSslVerification() {
        // TODO Auto-generated method stub
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType){}
                        public void checkServerTrusted(X509Certificate[] certs, String authType){}
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session){
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
