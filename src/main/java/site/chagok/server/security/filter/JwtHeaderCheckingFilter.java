package site.chagok.server.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import site.chagok.server.security.constants.SecurityHeader;
import site.chagok.server.security.service.JWTTokenService;
import site.chagok.server.security.util.ResponseUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHeaderCheckingFilter extends OncePerRequestFilter {

    private final JWTTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtHeader = request.getHeader(SecurityHeader.JWT_HEADER);

        // 만약 header에 jwtToken이 들어온 경우
        if (jwtHeader != null && jwtHeader.startsWith("Bearer ")) {
            String jwtToken = jwtHeader.substring(7);

            try {
                // jwt 기반, UsernamePasswordAuthenticationToken 생성
                Authentication userAuthToken = jwtTokenService.validateJwtToken(jwtToken);

                // Security Context에 추가.
                SecurityContextHolder.getContext().setAuthentication(userAuthToken);
            } catch (JwtException e) {
                if (!request.getRequestURI().equals("/auth/refresh") && e instanceof ExpiredJwtException) {// jwt 유효기간 만료
                    ResponseUtil.jwtExpiredJsonResponse(response);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

