package idorm.idormServer.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import idorm.idormServer.auth.adapter.in.web.AuthInterceptor;
import idorm.idormServer.auth.adapter.in.web.AuthResponseArgumentResolver;
import idorm.idormServer.auth.application.port.in.JwtTokenUseCase;
import idorm.idormServer.common.performance.QueryCountInterceptor;
import lombok.RequiredArgsConstructor;

@Profile("local")
@Configuration
@RequiredArgsConstructor
public class WebMvcLocalConfig implements WebMvcConfigurer {

	private final QueryCountInterceptor queryCountInterceptor;
	private final AuthInterceptor authInterceptor;
	private final JwtTokenUseCase jwtTokenUseCase;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor)
			.order(1)
			.addPathPatterns("/**")
			.excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**")
			.excludePathPatterns("/api/v1/email/**")
			.excludePathPatterns("/api/v1/signup", "/api/v1/signin", "/api/v1/refresh")
			.excludePathPatterns("/api/v1/members/me/password")
			.excludePathPatterns("/test/**");

		registry.addInterceptor(queryCountInterceptor)
			.order(2)
			.addPathPatterns("/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AuthResponseArgumentResolver(jwtTokenUseCase));
	}
}