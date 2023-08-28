package site.chagok.server.security.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.chagok.server.security.dto.SignInResponseDto;

@Getter
@Setter
public class AuthInfo {

    String jwtToken;
    String refreshToken;
    boolean signUp;

    public AuthInfo(String jwtToken, String refreshToken) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
    }

    public SignInResponseDto getSignInResDto() {
        return new SignInResponseDto(this.jwtToken, this.signUp);
    }
}
