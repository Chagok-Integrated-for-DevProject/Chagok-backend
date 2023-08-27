package site.chagok.server.security.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel(value = "JwtTokenSetDto", description = "jwt 토큰, 리프레시 할 시, jwt token과 refresh token 둘 다 필요")
public class JwtTokenSetDto {

    @ApiModelProperty(name = "jwtToken", value = "jwt토큰", example = "qwert1234...")
    private String jwtToken;
    @ApiModelProperty(name = "refreshToken", value = "리프레시 토큰", example = "qwert1234...")
    private String refreshToken;
}
