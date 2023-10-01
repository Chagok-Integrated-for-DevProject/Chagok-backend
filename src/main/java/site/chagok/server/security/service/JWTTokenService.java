package site.chagok.server.security.service;

import lombok.RequiredArgsConstructor;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JWTTokenService {

    private final RsaJsonWebKey rsaJsonWebKey;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthInfo issueJWTToken(String email, List<String> roles) {
        rsaJsonWebKey.setKeyId("k1");

        JwtClaims claims = new JwtClaims();
        claims.setIssuer("chagok service server");  // 토큰 발행자
        claims.setExpirationTimeMinutesInTheFuture(1); // 한시간 이후 토큰 expired
        claims.setGeneratedJwtId(); // jwt 토큰 고유 id
        claims.setIssuedAtToNow();  // jwt 토큰 발행 시간
        // claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
        claims.setClaim("email", email); // 사용자 식별 이메일 값

        claims.setStringListClaim("roles", roles); // 유저권한

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson()); // jwt payload 설정
        jws.setKey(rsaJsonWebKey.getPrivateKey()); // 암호화 할 키설정(rsa)
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId()); // 식별 key id
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256); // jwt 토큰 암호화 방식 설정 (헤더)

        String jwt;
        try {
            // jwt compact
            jwt = jws.getCompactSerialization();

            // refresh token 생성
            String refreshTokenValue = UUID.randomUUID().toString();

            RefreshToken refreshToken = RefreshToken.builder()
                    .refreshToken(refreshTokenValue)
                    .jwtId(claims.getJwtId())
                    .roles(roles)
                    .build();

            // redis 에 저장
            refreshTokenRepository.save(refreshToken);

            return new AuthInfo(jwt, refreshTokenValue, true);

        } catch (JoseException | MalformedClaimException e) {
            throw new AuthorizationApiException("auth_01", "access token issue error");
        }
    }

    public Authentication validateJwtToken(String jwt) throws InvalidJwtException {

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // jwt expired time 유무 검사
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setExpectedIssuer("chagok service server") // issuer 검사
                .setVerificationKey(rsaJsonWebKey.getKey()) // public key 검증
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) // 알고리즘 방식 검사
                .build();

        //  jwt validating
        JwtClaims jwtClaims = null;
        jwtClaims = jwtConsumer.processToClaims(jwt);

        String jwtUserEmail = jwtClaims.getClaimValue("email").toString();

        // 권한 설정
        List<GrantedAuthority> roles = getRoles((List<String>) jwtClaims.getClaimValue("roles"));
        User jwtClient = new User(jwtUserEmail, "", roles);

        return new UsernamePasswordAuthenticationToken(jwtClient, "", roles); // authentication token return
    }

    private List<GrantedAuthority> getRoles(List<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public AuthInfo renewRefreshToken(JwtTokenSetDto jwtTokenSetDto) {

        RefreshToken savedRefreshToken = refreshTokenRepository.findByRefreshToken(jwtTokenSetDto.getRefreshToken()).orElseThrow(() -> new AuthorizationApiException("refresh_01", "invalid refresh token"));

        /*
            1. 요청한 refreshToken 값과 저장 된 refresh token 값을 비교
            2. 저장 된 refresh token의 jwt id 값과  요청한 jwt id 값을 비교
         */

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // jwt expired time 유무 검사
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setExpectedIssuer("chagok service server") // issuer 검사
                .setVerificationKey(rsaJsonWebKey.getKey()) // public key 검증
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) // 알고리즘 방식 검사
                .build();

        JwtClaims jwtClaims = null;
        try {
            //  jwt validating
            jwtClaims = jwtConsumer.processToClaims(jwtTokenSetDto.getJwtToken());

            /*
                아직 jwt 토큰의 유효기간이 남아있거나,
                서버측 refresh token과 요청 refresh token 불일치,
                refresh 의 jwt Id와 요청 jwt 토큰의 id 불일치 시 에러
             */
            throw new AuthorizationApiException("refresh_02", "invalid request or invalid refresh token");

        } catch (InvalidJwtException e) {
            if (e.hasExpired()) {

                if (!jwtTokenSetDto.getRefreshToken().equals(savedRefreshToken.getRefreshToken())) {
                    throw new AuthorizationApiException("refresh_02", "invalid request or invalid refresh token");
                }

                String jwtUserEmail = jwtClaims.getClaimValue("email").toString();
                List<String> roles = (List<String>) jwtClaims.getClaimValue("roles");

                // 기존 refresh token 삭제
                refreshTokenRepository.delete(savedRefreshToken);

                // jwt 토큰, refresh 토큰 새로 발급
                return this.issueJWTToken(jwtUserEmail, roles);
            }
        }

        return null;
    }

    private boolean isNotExpiredTime(NumericDate secondsTimeStamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(secondsTimeStamp.getValue()), TimeZone.getDefault().toZoneId())
                .isAfter(LocalDateTime.now());
    }
}
