package idorm.idormServer.auth.adapter.in.web;

import static idorm.idormServer.auth.entity.RoleType.*;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import idorm.idormServer.auth.adapter.out.AuthResponseCode;
import idorm.idormServer.auth.adapter.out.exception.AccessDeniedAdminException;
import idorm.idormServer.auth.adapter.out.exception.UnAuthorizedAccessTokenException;
import idorm.idormServer.auth.application.port.in.JwtTokenUseCase;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.entity.RoleType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

	private final JwtTokenUseCase jwtTokenUseCase;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		if (auth == null) {
			return true;
		}

		if (notExistHeader(request)) {
			response.setStatus(AuthResponseCode.UNAUTHORIZED_ACCESS_TOKEN.getStatus().value());
			throw new UnAuthorizedAccessTokenException();
		}

		String token = AuthorizationExtractor.extractAccessToken(request);

		if (isInvalidToken(token)) {
			response.setStatus(AuthResponseCode.UNAUTHORIZED_ACCESS_TOKEN.getStatus().value());
			throw new UnAuthorizedAccessTokenException();
		}

		AuthResponse authInfo = jwtTokenUseCase.getParsedClaims(token);
		if (isAdminOnly(auth)) {
			if (isNotAdmin(authInfo.getRole())) {
				response.setStatus(AuthResponseCode.ACCESS_DENIED_ADMIN.getStatus().value());
				throw new AccessDeniedAdminException();
			}
		}
		return true;
	}

	private boolean notExistHeader(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		return Objects.isNull(authorizationHeader);
	}

	private boolean isInvalidToken(String token) {
		return !jwtTokenUseCase.isValid(token);
	}

	private boolean isAdminOnly(Auth auth) {
		return auth.role().compareTo(RoleType.ADMIN) == 0;
	}
}