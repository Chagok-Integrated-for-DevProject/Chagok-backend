package site.chagok.server.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chagok.server.common.contstans.SocialType;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String email;
    private SocialType socialType;

    public static OAuthAttributes of(String ragistrationId, String userNameAttributeName, Map<String, Object> attributes) {

        if (ragistrationId.equals("google"))
            return ofGoogle(userNameAttributeName, attributes);
        else if (ragistrationId.equals("kakao"))
            return ofKakao(userNameAttributeName, attributes);

        return null;
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .email(String.valueOf(attributes.get("email")))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .socialType(SocialType.Google)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {

        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");

        return OAuthAttributes.builder()
                .email(String.valueOf(kakaoAccount.get("email")))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .socialType(SocialType.Kakao)
                .build();
    }
}
