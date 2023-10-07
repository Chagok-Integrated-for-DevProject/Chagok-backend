package site.chagok.server.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "jwt 토큰, 리프레시 할 시, jwt token과 refresh token 둘 다 필요")
public class JwtTokenSetDto {

    @Schema(description = "jwt토큰", example = "qwert1234...")
    private String jwtToken;
    @Schema(description = "리프레시 토큰", example = "qwert1234...")
    private String refreshToken;
}
