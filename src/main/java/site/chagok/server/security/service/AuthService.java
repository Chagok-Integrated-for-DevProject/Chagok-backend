package site.chagok.server.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.chagok.server.common.contstans.SocialType;
import site.chagok.server.security.dto.JwtTokenSetDto;
import site.chagok.server.security.dto.ReqSignInDto;
import site.chagok.server.security.domain.AuthInfo;
import site.chagok.server.security.dto.SignUpDto;

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
         switch (socialType) {
             case Google:
                 return oAuthService.getGoogleCredential(accessToken);
             case Kakao:
                 return oAuthService.getKakaoResponse(accessToken);
             default:
                 throw new IllegalStateException("social type error");
         }
    }
}
