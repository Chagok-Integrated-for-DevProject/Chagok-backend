package site.chagok.server.security.redis.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

@RedisHash(value = "refreshToken", timeToLive = 172800)
@Builder
@ToString
@Getter
public class RefreshToken {

    @Id
    private String id;

    private String jwtId;

    private List<String> roles;

    @Indexed
    private String refreshToken;
}
