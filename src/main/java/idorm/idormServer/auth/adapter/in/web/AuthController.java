package idorm.idormServer.auth.adapter.in.web;

import static idorm.idormServer.auth.adapter.out.api.AuthResponseCode.*;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.auth.adapter.in.api.AuthorizationExtractor;
import idorm.idormServer.auth.adapter.out.api.AuthResponseCode;
import idorm.idormServer.auth.application.port.in.AuthUseCase;
import idorm.idormServer.auth.application.port.in.JwtTokenUseCase;
import idorm.idormServer.auth.application.port.in.RefreshTokenUseCase;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.application.port.in.dto.LoginRequest;
import idorm.idormServer.auth.domain.Auth;
import idorm.idormServer.auth.domain.AuthInfo;
import idorm.idormServer.common.response.SuccessResponse;
import idorm.idormServer.member.adapter.out.exception.NotFoundMemberException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "8. Auth", description = "회원 인증 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

	private final AuthUseCase authUseCase;
	private final JwtTokenUseCase jwtTokenUseCase;
	private final RefreshTokenUseCase refreshTokenUseCase;

	@Operation(summary = "회원 및 관리자 로그인")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "MEMBER_LOGIN"),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_LOGIN_INFO"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@PostMapping("/signin")
	public ResponseEntity<SuccessResponse<Object>> login(
		HttpServletResponse httpServletResponse,
		@Valid @RequestBody LoginRequest request
	) {
		AuthResponse authResponse = authUseCase.login(request);

		String accessToken = jwtTokenUseCase.createAccessToken(authResponse);
		String refreshToken = jwtTokenUseCase.createRefreshToken();
		refreshTokenUseCase.saveToken(refreshToken, authResponse.getId());

		httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		httpServletResponse.setHeader("Refresh-Token", "Bearer " + refreshToken);

		return ResponseEntity.ok().body(SuccessResponse.from(AuthResponseCode.MEMBER_LOGIN));
	}

	@Auth
	@Operation(summary = "회원 액세스 토큰 재발급 완료", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION),
		@SecurityRequirement(name = "Refresh-Token")})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "MEMBER_REFRESH"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN / UNAUTHORIZED_REFRESH_TOKEN"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_TOKEN"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@GetMapping("/refresh")
	public ResponseEntity<SuccessResponse<Object>> refresh(
		HttpServletRequest servletRequest,
		HttpServletResponse httpServletResponse,
		@AuthInfo AuthResponse authResponse
	) {
		validateExistHeader(servletRequest);
		Long memberId = authResponse.getId();
		String refreshToken = AuthorizationExtractor.extractRefreshToken(servletRequest);

		refreshTokenUseCase.matches(refreshToken, memberId);
		String reissueAccessToken = jwtTokenUseCase.createAccessToken(authResponse);

		httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + reissueAccessToken);

		return ResponseEntity.ok().body(SuccessResponse.from(AuthResponseCode.MEMBER_REFRESH));
	}

	@Auth
	@Operation(summary = "로그아웃", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "MEMBER_LOGOUT"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_PASSWORD"),
		@ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@GetMapping("/logout")
	public ResponseEntity<SuccessResponse<Object>> logout(
		@AuthInfo AuthResponse authResponse
	) {
		refreshTokenUseCase.deleteToken(authResponse.getId());

		return ResponseEntity.ok().body(SuccessResponse.from(MEMBER_LOGOUT));
	}

	private void validateExistHeader(HttpServletRequest request) {

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String refreshTokenHeader = request.getHeader("Refresh-Token");

		if (Objects.isNull(authorizationHeader) || Objects.isNull(refreshTokenHeader)) {
			throw new NotFoundMemberException();
		}
	}
}
