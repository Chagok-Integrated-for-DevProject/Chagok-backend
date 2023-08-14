package site.chagok.server.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import site.chagok.server.member.service.MemberService;
import site.chagok.server.security.constants.SecutiryHeader;
import site.chagok.server.security.dto.ChagokOAuth2User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTTokenService jwtTokenService;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        ChagokOAuth2User oAuth2User = (ChagokOAuth2User)authentication.getPrincipal();

        // DB에 없을 경우 회원가입
        memberService.signUp(oAuth2User);

        // jwt토큰 발급
        String jwtToken = jwtTokenService.constructJWTToken(oAuth2User.getName(), (Set<GrantedAuthority>) oAuth2User.getAuthorities());

        // 헤더 설정
        response.setHeader(SecutiryHeader.JWT_HEADER, jwtToken);

        log.info("generated jwtToken: " + response.getHeader(SecutiryHeader.JWT_HEADER));

        // 테스트용
        getRedirectStrategy().sendRedirect(request, response, "/api/oauthSuccess");
    }
}

