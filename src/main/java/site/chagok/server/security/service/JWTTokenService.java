package site.chagok.server.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class JWTTokenService {

    private final RsaJsonWebKey rsaJsonWebKey;

    public String constructJWTToken(String email, Set<GrantedAuthority> roles) {
        rsaJsonWebKey.setKeyId("k1");

        JwtClaims claims = new JwtClaims();
        claims.setIssuer("chagok service server");  // 토큰 발행자
        claims.setExpirationTimeMinutesInTheFuture(60); // 한시간 이후 토큰 expired
        claims.setGeneratedJwtId(); // jwt 토큰 고유 id
        claims.setIssuedAtToNow();  // jwt 토큰 발행 시간
        // claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
        claims.setClaim("email", email); // 사용자 식별 이메일 값

        List<String> userRoles = new ArrayList<>();

        for (GrantedAuthority role : roles) {
            userRoles.add(role.getAuthority());
        }
        claims.setStringListClaim("roles", userRoles); // 유저권한


        JsonWebSignature jws = new JsonWebSignature();
        // jwt payload 설정
        jws.setPayload(claims.toJson());
        // 암호화 할 키설정(rsa)
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        // 식별 key id
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        // jwt 토큰 암호화 방식 설정 (헤더)
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        String jwt;
        try {
            jwt = jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }

        return jwt;
    }

    public Authentication validateJwtToken(String jwt) throws InvalidJwtException {

        // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
        // be used to validate and process the JWT.
        // The specific validation requirements for a JWT are context dependent, however,
        // it is typically advisable to require a (reasonable) expiration time, a trusted issuer, and
        // an audience that identifies your system as the intended recipient.
        // If the JWT is encrypted too, you need only provide a decryption key or
        // decryption key resolver to the builder.
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // jwt expired time 유무 검사
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setExpectedIssuer("chagok service server") // issuer 검사
                .setVerificationKey(rsaJsonWebKey.getKey()) // public key 검증
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) // 알고리즘 방식 검사
                .build();

        //  jwt validating
        JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
        log.info("JWT validation succeeded! " + jwtClaims);

        String jwtClientEmail = jwtClaims.getClaimValue("email").toString();

        Collection<? extends GrantedAuthority> authority = Collections.singleton(new SimpleGrantedAuthority("USER")); // 권한 설정
        User jwtClient = new User(jwtClientEmail, "", authority);

        return new UsernamePasswordAuthenticationToken(jwtClient, "", authority); // authentication token return
    }
}
