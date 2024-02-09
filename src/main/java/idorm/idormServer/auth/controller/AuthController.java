package idorm.idormServer.auth.controller;

import idorm.idormServer.auth.dto.AuthInfo;
import idorm.idormServer.auth.dto.LoginRequest;
import idorm.idormServer.auth.service.AuthService;
import idorm.idormServer.auth.service.RefreshTokenService;
import idorm.idormServer.common.dto.DefaultResponseDto;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.support.token.AuthorizationExtractor;
import idorm.idormServer.support.token.Login;
import idorm.idormServer.support.token.TokenManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final TokenManager tokenManager;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, TokenManager tokenManager,
                          RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.tokenManager = tokenManager;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<DefaultResponseDto<Object>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthInfo authInfo = authService.login(request);
        String accessToken = tokenManager.createAccessToken(authInfo);
        String refreshToken = tokenManager.createRefreshToken();
        refreshTokenService.saveToken(refreshToken, authInfo.getId());

        return ResponseEntity.status(200)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header("Refresh-Token", "Bearer " + refreshToken)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_LOGIN")
                        .responseMessage("회원 로그인 완료")
                        .build());
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<DefaultResponseDto<Object>> refresh(
            HttpServletRequest request, @Login AuthInfo authInfo
    ) {
        validateExistHeader(request);
        Long memberId = authInfo.getId();
        String refreshToken = AuthorizationExtractor.extractRefreshToken(request);

        refreshTokenService.matches(refreshToken, memberId);
        String accessToken = tokenManager.createAccessToken(authInfo);

        return ResponseEntity.status(200)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_REFRESH")
                        .responseMessage("회원 액세스 토큰 재발급 완료")
                        .build());
    }

    @Operation(summary = "로그아웃 / FCM 제거 용도",
            description = "- FCM 토큰 관리를 위해 로그아웃 시 해당 API를 호출해주세요.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "MEMBER_LOGOUT"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_PASSWORD"),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @GetMapping("/logout")
    public ResponseEntity<DefaultResponseDto<Object>> logout(
            @Login AuthInfo authInfo
    ) {
        refreshTokenService.deleteToken(authInfo.getId());
        // TODO: FCM 제거

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_LOGOUT")
                        .responseMessage("회원 로그아웃 완료")
                        .build());
    }

    private void validateExistHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshTokenHeader = request.getHeader("Refresh-Token");
        if (Objects.isNull(authorizationHeader) || Objects.isNull(refreshTokenHeader)) {
            throw new CustomException(null, ExceptionCode.UNAUTHORIZED_MEMBER);
        }
    }
}
