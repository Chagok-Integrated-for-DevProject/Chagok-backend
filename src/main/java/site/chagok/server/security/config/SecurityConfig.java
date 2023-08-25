package site.chagok.server.security.config;

import lombok.RequiredArgsConstructor;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.http.SecurityHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import site.chagok.server.security.constants.SecutiryHeader;
import site.chagok.server.security.filter.JwtHeaderCheckingFilter;
import site.chagok.server.security.service.ChagokOAuth2UserService;
import site.chagok.server.security.service.OAuth2SuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

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
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new AuthenticationEntryPoint())) // 인증받지 않은 사용자에 대한 처리
                .oauth2Login(oauth2 -> oauth2
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(chagokOAuth2UserService)) // oauth 사용자 정보 얻음
                                .successHandler(oAuth2SuccessHandler)) // 인증 성공시, 헤더에 jwt 발급 및 redirect
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless 세팅
                .addFilterAfter(jwtHeaderCheckingFilter, BasicAuthenticationFilter.class) // jwt 헤더 검사
                .csrf().disable() // test
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic().disable();

        return http.build();
    }


    // chagok.site 간 cors config
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("https://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE", "PUT"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of(SecutiryHeader.JWT_HEADER)); // custom 설정 중 해당 헤더만 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    // 로그인 방식 설정
    public class AuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
        public AuthenticationEntryPoint()  {
            super("");
        }
        // 인증받지 않은 사용자에 대해, 401 error - jwt 토큰으로만 인증
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
            response.sendError(401, "Unauthorized");
        }
    }
}
