package site.chagok.server.security.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Schema(description = "인증 성공 응답 - jwt 토큰, refresh 토큰, 회원가입 유무")
@AllArgsConstructor
public class ResSignInDto {

    @Schema(description = "jwt토큰", example = "qwert1234...")
    String jwtToken;

    @Schema(description = "회원가입 유무(상태, 가입이 된 회원이라면 true, 가입이 안된 회원이라면 false)", example = "true or false")
    @JsonProperty("isSignUp")
    boolean isSignUp;
}
