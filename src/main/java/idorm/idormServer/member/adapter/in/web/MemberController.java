package idorm.idormServer.member.adapter.in.web;

import static idorm.idormServer.member.adapter.out.MemberResponseCode.*;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.domain.Auth;
import idorm.idormServer.auth.domain.AuthInfo;
import idorm.idormServer.common.response.SuccessResponse;
import idorm.idormServer.member.application.port.in.MemberPhotoUseCase;
import idorm.idormServer.member.application.port.in.MemberUseCase;
import idorm.idormServer.member.application.port.in.dto.MemberInfoResponse;
import idorm.idormServer.member.application.port.in.dto.NicknameUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.PasswordUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "8. Member", description = "회원 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

	private final MemberUseCase memberUseCase;
	private final MemberPhotoUseCase memberPhotoUseCase;

	@Operation(summary = "회원 가입")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201", description = "MEMBER_REGISTERED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400",
			description = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_EMAIL"),
		@ApiResponse(responseCode = "404", description = "EMAIL_NOT_FOUND"),
		@ApiResponse(responseCode = "409",
			description = "DUPLICATE_EMAIL / DUPLICATE_NICKNAME"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/signup")
	public ResponseEntity<SuccessResponse<Object>> signUp(
		@Valid @RequestBody SignupRequest request
	) {
		memberUseCase.signUp(request);
		return ResponseEntity.status(201).body(SuccessResponse.from(MEMBER_REGISTERED));
	}

	@Auth
	@Operation(summary = "내 정보 조회", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "MEMBER_FOUND",
			content = @Content(schema = @Schema(implementation = MemberInfoResponse.class))),
		@ApiResponse(responseCode = "400", description = "FILED_REQUIRED"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_EMAIL"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_EMAIL"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@GetMapping("/members/me")
	public ResponseEntity<SuccessResponse<Object>> getMyInfo(
		@AuthInfo AuthResponse authResponse
	) {
		MemberInfoResponse memberInfoResponse = memberUseCase.getInfo(authResponse);
		return ResponseEntity.ok().body(SuccessResponse.of(MEMBER_FOUND, memberInfoResponse));
	}

	@Auth
	@Operation(summary = "닉네임 변경", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "NICKNAME_UPDATED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400", description = "FILED_REQUIRED / INVALID_NICKNAME_CHARACTER"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER"),
		@ApiResponse(responseCode = "409", description = "DUPLICATED_NICKNAME"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@PatchMapping("/members/me/nickname")
	public ResponseEntity<SuccessResponse<Object>> editNickname(
		@AuthInfo AuthResponse authResponse, @RequestBody @Valid NicknameUpdateRequest request
	) {
		memberUseCase.editNickname(authResponse, request);
		return ResponseEntity.ok().body(SuccessResponse.from(NICKNAME_UPDATED));
	}

	@Operation(summary = "비밀번호 변경")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "PASSWORD_UPDATED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED / INVALID_PASSWORD_CHARACTER"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_EMAIL / EXPIRED_EMAIL_VERIFICATION_CODE"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_EMAIL"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@PatchMapping("/members/me/password")
	public ResponseEntity<SuccessResponse<Object>> editPassword(
		@RequestBody @Valid PasswordUpdateRequest request
	) {
		memberUseCase.editPassword(request);
		return ResponseEntity.ok().body(SuccessResponse.from(PASSWORD_UPDATED));
	}

	@Auth
	@Operation(summary = "회원 탈퇴", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "MEMBER_DELETED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@DeleteMapping("/members/me")
	public ResponseEntity<SuccessResponse<Object>> deleteAccount(
		@AuthInfo AuthResponse authResponse
	) {
		memberUseCase.withdraw(authResponse);
		return ResponseEntity.ok().body(SuccessResponse.from(MEMBER_DELETED));
	}

	@Auth
	@Operation(summary = "프로필 사진 저장", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201", description = "PROFILE_PHOTO_SAVED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_FILE"),
		@ApiResponse(responseCode = "413", description = "EXCEED_FILE_SIZE"),
		@ApiResponse(responseCode = "415", description = "UNSUPPORTED_FILE_TYPE"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR / S3_SERVER_ERROR"),
	})
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/members/me/profile-photo")
	public ResponseEntity<SuccessResponse<Object>> saveMemberPhoto(
		@AuthInfo AuthResponse authResponse,
		@RequestPart(value = "file", required = false) MultipartFile file
	) {
		memberPhotoUseCase.savePhoto(authResponse, file);
		return ResponseEntity.status(201).body(SuccessResponse.from(PROFILE_PHOTO_SAVED));
	}

	@Auth
	@Operation(summary = "프로필 사진 삭제", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "PROFILE_PHOTO_DELETED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_FILE"),
		@ApiResponse(responseCode = "413", description = "EXCEED_FILE_SIZE"),
		@ApiResponse(responseCode = "415", description = "UNSUPPORTED_FILE_TYPE"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR / S3_SERVER_ERROR"),
	})
	@DeleteMapping("/members/me/profile-photo")
	public ResponseEntity<SuccessResponse<Object>> deleteMemberPhoto(
		@AuthInfo AuthResponse authResponse
	) {
		memberPhotoUseCase.deletePhoto(authResponse);
		return ResponseEntity.ok().body(SuccessResponse.from(PROFILE_PHOTO_DELETED));
	}
}