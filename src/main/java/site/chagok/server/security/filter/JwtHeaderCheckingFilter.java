package site.chagok.server.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import site.chagok.server.security.constants.SecutiryHeader;
import site.chagok.server.security.service.JWTTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHeaderCheckingFilter extends OncePerRequestFilter {

    private final JWTTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtHeader = request.getHeader(SecutiryHeader.JWT_HEADER);

        // 만약 header에 jwtToken이 들어온 경우
        if (jwtHeader != null && jwtHeader.startsWith("Bearer ")) {
            String jwtToken = jwtHeader.substring(7);

            // 테스트 코드
            if (jwtToken.equals("12345")) {
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(
                                "ydg98381@gmail.com", null, Collections.singleton(new SimpleGrantedAuthority("USER"))));
            } else {
                try {
                    // jwt 기반, UsernamePasswordAuthenticationToken 생성
                    Authentication userAuthToken = jwtTokenService.validateJwtToken(jwtToken);

                    // Security Context에 추가.
                    SecurityContextHolder.getContext().setAuthentication(userAuthToken);
                } catch (InvalidJwtException e) {

                    if (e.hasExpired()) { // jwt 유효기간 만료

                    } else { //

                    }
                    throw new RuntimeException(e);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

