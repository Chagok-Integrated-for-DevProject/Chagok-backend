package site.chagok.server.security.config;

import lombok.RequiredArgsConstructor;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import site.chagok.server.security.filter.JwtHeaderCheckingFilter;
import site.chagok.server.security.service.ChagokOAuth2UserService;
import site.chagok.server.security.service.OAuth2SuccessHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtHeaderCheckingFilter jwtHeaderCheckingFilter;
    private final ChagokOAuth2UserService chagokOAuth2UserService;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/member/update/**", "/member/info", "/contests/comments").authenticated()
                        .anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                                .userInfoEndpoint(userInfo ->
                                        userInfo.userService(chagokOAuth2UserService)) // oauth 사용자 정보 얻음
                                .successHandler(oAuth2SuccessHandler)) // 인증 성공시, 헤더에 jwt 발급 및 redirect
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless 세팅
                .csrf().disable()
                .addFilterAfter(jwtHeaderCheckingFilter, BasicAuthenticationFilter.class); // jwt 헤더 검사

        return http.build();
    }
}
