package site.chagok.server.security.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
    }

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
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {

        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");

        return OAuthAttributes.builder()
                .email(String.valueOf(kakaoAccount.get("email")))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
