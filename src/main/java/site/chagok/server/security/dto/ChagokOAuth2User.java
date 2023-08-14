package site.chagok.server.security.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;
import site.chagok.server.common.contstans.contstans.SocialType;

import java.io.Serializable;
import java.util.*;

public class ChagokOAuth2User implements OAuth2User, Serializable {

    private final Set<GrantedAuthority> authorities;

    private final Map<String, Object> attributes;

    private final String nameAttributeKey;
    private String email;
    private SocialType socialType;


    public ChagokOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
                            String nameAttributeKey, String email, SocialType socialType) {
        Assert.notEmpty(attributes, "attributes cannot be empty");

        if (!attributes.containsKey(nameAttributeKey)) {
            throw new IllegalArgumentException("Missing attribute '" + nameAttributeKey + "' in attributes");
        }
        this.authorities = (authorities != null)
                ? Collections.unmodifiableSet(new LinkedHashSet<>(this.sortAuthorities(authorities)))
                : Collections.unmodifiableSet(new LinkedHashSet<>(AuthorityUtils.NO_AUTHORITIES));
        this.attributes = Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.socialType = socialType;
    }
    private Set<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
                Comparator.comparing(GrantedAuthority::getAuthority));
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return email;
    }

    public SocialType getSocialType() {
        return this.socialType;
    }
}
