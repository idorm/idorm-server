package idorm.idormServer.auth.adapter.in.web;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import idorm.idormServer.auth.adapter.out.exception.UnAuthorizedAccessTokenException;
import idorm.idormServer.auth.application.port.in.JwtTokenUseCase;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthResponseArgumentResolver implements HandlerMethodArgumentResolver {

	private final JwtTokenUseCase jwtTokenUseCase;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthInfo.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String token = AuthorizationExtractor.extractAccessToken(Objects.requireNonNull(request));
		if (token == null) {
			throw new UnAuthorizedAccessTokenException();
		}
		return jwtTokenUseCase.getParsedClaims(token);
	}
}