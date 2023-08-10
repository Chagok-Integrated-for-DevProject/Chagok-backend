package site.chagok.server.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwk.RsaJsonWebKey;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import site.chagok.server.security.constants.SecutiryHeader;
import site.chagok.server.security.dto.ChagokOAuth2User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTTokenService jwtTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        ChagokOAuth2User oAuth2User = (ChagokOAuth2User)authentication.getPrincipal();

        String jwtToken = jwtTokenService.constructJWTToken(oAuth2User.getName(), (Set<GrantedAuthority>) oAuth2User.getAuthorities());

        response.setHeader(SecutiryHeader.JWT_HEADER, jwtToken);

        log.info("generated jwtToken: " + response.getHeader(SecutiryHeader.JWT_HEADER));


        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path("/api/oauthSuccess")
                .queryParam("jwtToken", jwtToken)
                .build();

        getRedirectStrategy().sendRedirect(request, response, "/api/oauthSuccess");
    }
}

