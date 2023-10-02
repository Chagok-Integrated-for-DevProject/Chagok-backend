package site.chagok.server.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import site.chagok.server.common.exception.AuthorizationApiException;
import site.chagok.server.security.dto.JwtTokenSetDto;
import site.chagok.server.security.domain.AuthInfo;
import site.chagok.server.security.redis.domain.RefreshToken;
import site.chagok.server.security.redis.domain.RefreshTokenRepository;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JWTTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public AuthInfo issueJWTToken(String email, List<String> roles) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");
        headers.put("kid", "k1");

        String jti = UUID.randomUUID().toString();

        Claims claims = Jwts.claims()
                .setId(jti)
                .setIssuedAt(new Date())
                .setIssuer("chagok service server");
        claims.put("email", email);
        claims.put("roles", roles);
        claims.put("expired", getExpirationTimeMinutesInTheFuture(30));

        String jwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256).compact();

        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(refreshTokenValue)
                .jwtId(jti)
                .roles(roles)
                .build();

        // redis 에 저장
        refreshTokenRepository.save(refreshToken);

        return new AuthInfo(jwt, refreshTokenValue, true);
    }

    private Date getExpirationTimeMinutesInTheFuture(int minutes) {
        Date expiredTime = new Date();
        expiredTime.setTime(expiredTime.getTime() + 1000 * 60L * minutes);

        return expiredTime;
    }

    public Authentication validateJwtToken(String jwt) throws JwtException {

        JwtParser validator = Jwts.parserBuilder()
                .requireIssuer("chagok service server")
                .setSigningKey(key).build();

        //  jwt validating
        Claims jwtClaim = (Claims) validator.parse(jwt).getBody();

        if (isExpired(jwtClaim)) {
            Header jwtHeader = validator.parse(jwt).getHeader();
            throw new ExpiredJwtException(jwtHeader, jwtClaim, "expired token");
        }

        // 권한 설정
        List<GrantedAuthority> roles = getRoles((List<String>) jwtClaim.get("roles"));
        User jwtClient = new User((String)jwtClaim.get("email"), "", roles);

        return new UsernamePasswordAuthenticationToken(jwtClient, "", roles); // authentication token return
    }

    private List<GrantedAuthority> getRoles(List<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private boolean isExpired(Claims jwsClaims) {
        Date expiredDate = new Date(Long.parseLong(jwsClaims.get("expired").toString()));
        return expiredDate.before(new Date());
    }

    public AuthInfo renewRefreshToken(JwtTokenSetDto jwtTokenSetDto) {

        RefreshToken savedRefreshToken = refreshTokenRepository.findByRefreshToken(jwtTokenSetDto.getRefreshToken()).orElseThrow(() -> new AuthorizationApiException("refresh_01", "invalid refresh token"));

        /*
            1. 요청한 refreshToken 값과 저장 된 refresh token 값을 비교
            2. 저장 된 refresh token의 jwt id 값과  요청한 jwt id 값을 비교
         */

        JwtParser validator = Jwts.parserBuilder()
                .requireIssuer("chagok service server")
                .setSigningKey(key).build();

        Claims jwtPayload = null;
        try {
            jwtPayload = (Claims) validator.parse(jwtTokenSetDto.getJwtToken()).getBody();
        } catch (JwtException e) {
            // jwt 토큰 에러
            throw new AuthorizationApiException("jwt_01", "jwt validation error");
        }

        /*
            아직 jwt 토큰의 유효기간이 남아있거나,
            서버측 refresh token과 요청 refresh token 불일치,
            refresh 의 jwt Id와 요청 jwt 토큰의 id 불일치 시 에러
         */

        boolean isExpired = isExpired(jwtPayload);
        boolean equalRefreshToken = jwtTokenSetDto.getRefreshToken().equals(savedRefreshToken.getRefreshToken());
        boolean equalJti = savedRefreshToken.getJwtId().equals(jwtPayload.get("jti"));

        if (!isExpired || !equalRefreshToken || !equalJti) {
            throw new AuthorizationApiException("refresh_02", "invalid request or invalid refresh token");
        }

        String jwtUserEmail = jwtPayload.get("email", String.class);
        List<String> roles = jwtPayload.get("roles", List.class);

        refreshTokenRepository.delete(savedRefreshToken);

        return this.issueJWTToken(jwtUserEmail, roles);
    }
}
