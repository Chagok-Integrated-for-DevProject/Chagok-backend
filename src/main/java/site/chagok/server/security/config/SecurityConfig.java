package site.chagok.server.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import site.chagok.server.security.constants.SecutiryHeader;
import site.chagok.server.security.filter.JwtHeaderCheckingFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtHeaderCheckingFilter jwtHeaderCheckingFilter;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/member/update/**", "/member/info", "/contests/comments").authenticated()
                        .anyRequest().permitAll())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new AuthenticationEntryPoint())) // 인증받지 않은 사용자에 대한 처리
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless 세팅
                .addFilterAfter(jwtHeaderCheckingFilter, BasicAuthenticationFilter.class) // jwt 헤더 검사
                .csrf().disable() // test
                .cors(withDefaults())
                .httpBasic().disable();

        return http.build();
    }


    // chagok.site 간 cors config
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("https://localhost:3000", "http://localhost:3000", "https://localhost:3001"));
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
