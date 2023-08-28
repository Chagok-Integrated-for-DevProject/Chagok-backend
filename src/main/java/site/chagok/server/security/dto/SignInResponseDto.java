package site.chagok.server.security.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@ApiModel(value = "SignInResponseDto", description = "인증 성공 응답 - jwt 토큰, refresh 토큰, 회원가입 유무")
@AllArgsConstructor
public class SignInResponseDto {

    @ApiModelProperty(name = "jwtToken", value = "jwt토큰", example = "qwert1234...")
    String jwtToken;

    @ApiModelProperty(name = "signUp", value = "처음 로그인시 회원가입 유무( true or false )", example = "true or false")
    boolean signUp;
}
