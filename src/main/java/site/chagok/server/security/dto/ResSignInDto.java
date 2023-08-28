package site.chagok.server.security.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@ApiModel(value = "SignInResponseDto", description = "인증 성공 응답 - jwt 토큰, refresh 토큰, 회원가입 유무")
@AllArgsConstructor
public class ResSignInDto {

    @ApiModelProperty(value = "jwt토큰", example = "qwert1234...")
    String jwtToken;

    @ApiModelProperty(value = "회원가입 유무(상태, 가입이 된 회원이라면 true, 가입이 안된 회원이라면 false)", example = "true or false")
    @JsonProperty("isSignUp")
    boolean isSignUp;
}
