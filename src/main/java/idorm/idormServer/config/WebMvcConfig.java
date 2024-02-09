package idorm.idormServer.config;

import idorm.idormServer.common.interceptor.AuthInterceptor;
import idorm.idormServer.support.token.AuthenticationPrincipalArgumentResolver;
import idorm.idormServer.support.token.TokenManager;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final TokenManager tokenManager;

    public WebMvcConfig(AuthInterceptor authInterceptor, TokenManager tokenManager) {
        this.authInterceptor = authInterceptor;
        this.tokenManager = tokenManager;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**")
                .excludePathPatterns("/api/v1/auth/**")
                .excludePathPatterns("/api/v1/calendar/**")
                .excludePathPatterns("/api/v1/calendars")
                .excludePathPatterns("/test/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(tokenManager);
    }
}
