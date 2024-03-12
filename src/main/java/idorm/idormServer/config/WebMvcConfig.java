package idorm.idormServer.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import idorm.idormServer.auth.adapter.in.web.AuthInterceptor;
import idorm.idormServer.auth.adapter.in.web.AuthResponseArgumentResolver;
import idorm.idormServer.auth.application.port.in.JwtTokenUseCase;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final AuthInterceptor authInterceptor;
	private final JwtTokenUseCase jwtTokenUseCase;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**")
			.excludePathPatterns("/api/v1/email/**", "/api/v1/signup", "/api/v1/signin", "/api/v1/members/me/password")
			.excludePathPatterns("/api/error", "/test/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AuthResponseArgumentResolver(jwtTokenUseCase));
	}
}