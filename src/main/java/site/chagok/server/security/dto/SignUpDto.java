package site.chagok.server.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import site.chagok.server.common.contstans.SocialType;

import java.util.Set;

@Getter
@Setter
@Schema( description = "사용자 회원가입 - accessToken, 소셜타입, 닉네임, 설정할 기술스택")
public class SignUpDto {

    @Schema(description = "소셜로그인 액세스 토큰", example = "3212131214256123516")
    private String accessToken;

    @Schema(description = "소셜 로그인 종류", example = "only, Google or Kakao")
    private SocialType socialType;

    @Schema(description = "등록할 닉네임", example = "abcdefgh")
    private String nickName;

    @JsonProperty("skills")
    @Schema(description = "사용자 기술스택")
    private Set<String> techStacks;
}
