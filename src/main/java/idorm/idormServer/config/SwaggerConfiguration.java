package idorm.idormServer.config;

import com.fasterxml.classmate.TypeResolver;
import idorm.idormServer.community.dto.comment.CommentDefaultResponseDto;
import idorm.idormServer.community.dto.comment.CommentParentResponseDto;
import idorm.idormServer.community.dto.post.PostAbstractResponseDto;
import idorm.idormServer.community.dto.post.PostOneResponseDto;
import idorm.idormServer.email.dto.EmailDefaultResponseDto;

import idorm.idormServer.matching.dto.MatchingDefaultResponseDto;
import idorm.idormServer.matchingInfo.dto.MatchingInfoDefaultResponseDto;
import idorm.idormServer.member.dto.MemberDefaultResponseDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

    /**
     * swagger 문서 api 설정
     */
    @Bean
    public Docket api() {
        TypeResolver typeResolver = new TypeResolver();

        return new Docket(DocumentationType.OAS_30)
                .additionalModels(
                        typeResolver.resolve(EmailDefaultResponseDto.class),
                        typeResolver.resolve(MemberDefaultResponseDto.class),
                        typeResolver.resolve(MatchingInfoDefaultResponseDto.class),
                        typeResolver.resolve(MatchingDefaultResponseDto.class),
                        typeResolver.resolve(PostAbstractResponseDto.class),
                        typeResolver.resolve(PostOneResponseDto.class),
                        typeResolver.resolve(CommentDefaultResponseDto.class),
                        typeResolver.resolve(CommentParentResponseDto.class)
                        )
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors
                        .withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo()).securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));
    }

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
     * api 정보 설정 부분
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("idorm API")
                .version("1.0.0")
                .build();
    }
}