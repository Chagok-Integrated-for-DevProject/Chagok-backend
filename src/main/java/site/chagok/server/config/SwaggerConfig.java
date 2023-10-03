package site.chagok.server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String API_NAME = "CHAGOK API";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "CHAGOK API 명세서 " +
            "\n\n error [401 - invalid_01] : secure 명시되어 있는 api 접근시  error code 가능(인증 오류)" +
            "\n\n error [403 - jwt_02] :  token expired ";


    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION);

        final String securitySchemeName = "jwt Auth";

        // API 요청헤더에 인증정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securitySchemeName);
        // SecuritySchemes 등록
        Components components = new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

}
