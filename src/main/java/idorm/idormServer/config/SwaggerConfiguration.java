package idorm.idormServer.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.Arrays;
import java.util.List;

/**
 * http://localhost:8080/swagger-ui/#/
 */

@Configuration
public class SwaggerConfiguration {

    /**
     * swagger 문서 api 설정
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false) // Swagger에서 제공해주는 기본 응답 코드 (200, 401, 403, 404). false로 설정하면 기본 응답 코드를 노출하지 않습니다.
                .select()
                .apis(RequestHandlerSelectors.any()) // Swagger를 적용할 패키지 설정
                .paths(PathSelectors.any()) // Swagger를 적용할 주소 패턴을 세팅
                .build()
                .apiInfo(apiInfo()).securityContexts(Arrays.asList(securityContext())) // Swagger UI로 노출할 정보
                .securitySchemes(Arrays.asList(apiKey()));
    }

    /**
     * jwt를 통해서 인증/인가 설정을 했을 시, swagger에서도 해당 설정을 잡아주는 부분
     */
    private ApiKey apiKey() {

        return new ApiKey("X-AUTH-TOKEN", "X-AUTH-TOKEN", "header");
    }

    private SecurityContext securityContext() {
        return springfox
                .documentation
                .spi.service
                .contexts
                .SecurityContext
                .builder()
                .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("X-AUTH-TOKEN", authorizationScopes));
    }

    /**
     * jwt 인증을 위한 swagger 코드 end
     */

    /**
     * api 정보 설정 부분
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("idorm API")
                .version("1.0")
                .build();
    }
}