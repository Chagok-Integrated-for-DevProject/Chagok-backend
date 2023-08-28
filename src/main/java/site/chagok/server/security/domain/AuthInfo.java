package site.chagok.server.security.domain;

import lombok.*;
import site.chagok.server.security.dto.ResSignInDto;

@Getter
@Setter
@AllArgsConstructor
public class AuthInfo {

    private String jwtToken;
    private String refreshToken;
    private boolean isSignUp;

    public AuthInfo(boolean isSignUp) {
        this.isSignUp = isSignUp;
    }

    public ResSignInDto getSignInResDto() {
        return new ResSignInDto(this.jwtToken, this.isSignUp);
    }
}
