package idorm.idormServer.auth.adapter.in.api;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import idorm.idormServer.auth.adapter.out.api.exception.AccessDeniedAdminException;
import idorm.idormServer.auth.application.port.in.JwtTokenUseCase;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.domain.Auth;
import idorm.idormServer.auth.domain.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);

	private final JwtTokenUseCase jwtTokenUseCase;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod)handler;
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);

		if (Objects.isNull(auth)) {
			return true;
		}

		if (notExistHeader(request)) {
			LOGGER.info("no header" + request.getRequestURI());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}

		String token = AuthorizationExtractor.extractAccessToken(request);

		if (isInvalidToken(token)) {
			LOGGER.info("no token" + request.getRequestURI());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}

		AuthResponse authInfo = jwtTokenUseCase.getParsedClaims(token);
		if (isAdminOnly(auth)) {
			validateRoleAdmin(authInfo);
		}

		request.setAttribute("authInfo", authInfo);
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

	private void validateRoleAdmin(AuthResponse authInfo) {
		if (RoleType.ADMIN.isNot(authInfo.getRole())) {
			throw new AccessDeniedAdminException();
		}
	}
}