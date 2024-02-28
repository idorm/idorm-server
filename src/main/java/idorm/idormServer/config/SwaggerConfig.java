package idorm.idormServer.config;

import org.apache.http.HttpHeaders;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import idorm.idormServer.auth.adapter.in.api.AuthInfo;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {

		final Info info = new Info()
			.title("아이돔 2.1.0 api")
			.description("idorm api description")
			.version("2.1.0");

		final String accessTokenSchemaName = HttpHeaders.AUTHORIZATION;
		final String refreshTokenSchemaName = "Refresh-Token";
		final SecurityRequirement tokenRequirement = new SecurityRequirement()
			.addList(accessTokenSchemaName)
			.addList(refreshTokenSchemaName);

		final SecurityScheme accessTokenSecurityScheme = new SecurityScheme()
			.name(accessTokenSchemaName)
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.in(SecurityScheme.In.HEADER)
			.bearerFormat("JWT");
		final SecurityScheme refreshTokenSecurityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.APIKEY)
			.in(SecurityScheme.In.HEADER)
			.name(refreshTokenSchemaName)
			.scheme("bearer")
			.description("Access Token 재발급 시에만 필요합니다.")
			.bearerFormat("JWT");

		final Components components = new Components()
			.addSecuritySchemes(accessTokenSchemaName, accessTokenSecurityScheme)
			.addSecuritySchemes(refreshTokenSchemaName, refreshTokenSecurityScheme);

		return new OpenAPI()
			.info(info)
			.addSecurityItem(tokenRequirement)
			.components(components);
	}

	static {
		SpringDocUtils.getConfig().addAnnotationsToIgnore(AuthInfo.class);
	}
}