package site.chagok.server.security.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import site.chagok.server.security.dto.SignInResponseDto;
import site.chagok.server.security.domain.AuthInfo;

public class ResponseUtil {

    public static ResponseEntity<SignInResponseDto> createResponseWithCookieAndBody(AuthInfo authInfo) {
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authInfo.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/auth/refresh")
                .domain("api.chagok.site")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshCookie.toString()).body(authInfo.getSignInResDto());
    }
}
