package site.chagok.server.security.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import site.chagok.server.common.contstans.SocialType;

@Getter
@Setter
@ApiModel(value = "SignInRequestDto", description = "인증 요청 - 액세스 토큰, 소셜로그인")
public class ReqSignInDto {

    @ApiModelProperty(value = "액세스 토큰", example = "23109f0u3109u3910u3190u390u309u13...")
    String accessToken;
    @ApiModelProperty(value = "구글 or 카카오 중 하나", example = "Kakao or Google 값으로만 전달")
    SocialType socialType;
}
