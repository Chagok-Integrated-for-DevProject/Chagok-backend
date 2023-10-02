package site.chagok.server.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import site.chagok.server.common.dto.ErrorDto;
import site.chagok.server.common.exception.AuthorizationApiException;
import site.chagok.server.security.dto.ResSignInDto;
import site.chagok.server.security.domain.AuthInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    public static ResponseEntity<ResSignInDto> createResponseWithCookieAndBody(AuthInfo authInfo) {
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authInfo.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/auth/refresh")
                .domain("api.chagok.site")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(authInfo.getSignInResDto());
    }

    public static void jwtExpiredJsonResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonException = objectMapper.writeValueAsString(new ErrorDto(HttpServletResponse.SC_FORBIDDEN, "jwt_02", "access token is expired"));

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(jsonException);
    }

    public static void invalidMemberJsonResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonException = objectMapper.writeValueAsString(new ErrorDto(HttpServletResponse.SC_UNAUTHORIZED, "invalid_01", "invalid member or invalid access token"));

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonException);
    }
}
