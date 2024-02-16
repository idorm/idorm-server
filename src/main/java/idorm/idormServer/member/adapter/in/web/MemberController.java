package idorm.idormServer.member.adapter.in.web;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import idorm.idormServer.common.dto.DefaultResponseDto;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "8. Member", description = "회원 api")
@Validated
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberController {

	private final MemberUseCase memberUseCase;
	private final MemberPhotoUseCase memberPhotoUseCase;

	@Operation(summary = "회원 가입", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@Auth
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
	}
	)
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/auth/register")
	public ResponseEntity<DefaultResponseDto<Object>> signUp(
		@Valid @RequestBody SignupRequest request
	) {
		memberUseCase.signUp(request);

		return ResponseEntity.status(201)
			.body(DefaultResponseDto.builder()
				.responseCode("MEMBER_REGISTERED")
				.responseMessage("회원 가입 완료")
				.build());
	}

	@Auth
	@Operation(summary = "내 정보 조회")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "MEMBER_FOUND",
			content = @Content(schema = @Schema(implementation = MemberInfoResponse.class))),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@GetMapping("/member")
	public ResponseEntity<DefaultResponseDto<Object>> getMyInfo(
		@AuthInfo AuthResponse authResponse
	) {
		return ResponseEntity.status(200)
			.body(DefaultResponseDto.builder()
				.responseCode("MEMBER_FOUND")
				.responseMessage("회원 단건 조회 완료")
				.data(MemberInfoResponse.of(member))
				.build());
	}

	@Operation(summary = "닉네임 변경", description =
		"- 닉네임 형식은 영문, 숫자, 또는 한글의 조합 / 2-8 글자 입니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "NICKNAME_UPDATED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400",
			description = "FIELD_REQUIRED / NICKNAME_CHARACTER_INVALID / NICKNAME_LENGTH_INVALID"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "409",
			description = "DUPLICATE_NICKNAME / CANNOT_UPDATE_NICKNAME"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@PatchMapping("/member/nickname")
	public ResponseEntity<DefaultResponseDto<Object>> editNickname(
		@AuthInfo AuthResponse authResponse,
		@RequestBody @Valid NicknameUpdateRequest request) {

		memberUseCase.editNickname(member, request);

		return ResponseEntity.status(200)
			.body(DefaultResponseDto.builder()
				.responseCode("NICKNAME_UPDATED")
				.responseMessage("회원 닉네임 변경 완료")
				.build());
	}

	@Operation(summary = "비밀번호 변경", description = "- 이메일 API 에서 /verifyCode/password/{email} 인증 후 5분동안 비밀번호 변경 가능합니다.\n"
		+
		"- 비밀번호 변경용 이메일이 인증되지 않았다면 UNAUTHORIZED_EMAIL(401)을 던집니다. \n" +
		"- 비밀번호 변경용 이메일 인증 성공 시점으로 5분 후에 해당 API 요청 시 EXPIRED_CODE(401)를 던집니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "PASSWORD_UPDATED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400",
			description = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
		@ApiResponse(responseCode = "401",
			description = "UNAUTHORIZED_EMAIL / EXPIRED_CODE"),
		@ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@PatchMapping("/auth/password")
	public ResponseEntity<DefaultResponseDto<Object>> editPassword(
		@RequestBody @Valid PasswordUpdateRequest request) {

		memberUseCase.editPassword(request);

		return ResponseEntity.status(200)
			.body(DefaultResponseDto.builder()
				.responseCode("PASSWORD_UPDATED")
				.responseMessage("회원 비밀번호 변경 완료")
				.build());

	}

	@Operation(summary = "회원 탈퇴")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "MEMBER_DELETED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@DeleteMapping("/account")
	public ResponseEntity<DefaultResponseDto<Object>> deleteAccount(
		@AuthInfo AuthResponse authResponse
	) {
		memberUseCase.leave(member);

		return ResponseEntity.status(200)
			.body(DefaultResponseDto.builder()
				.responseCode("MEMBER_DELETED")
				.responseMessage("회원 탈퇴 완료")
				.build());
	}

	@Operation(summary = "프로필 사진 저장", description = "- 기존 프로필 사진을 삭제 후 새로운 프로필 사진을 저장합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201", description = "PROFILE_PHOTO_SAVED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "404", description = "FILE_NOT_FOUND"),
		@ApiResponse(responseCode = "413", description = "FILE_SIZE_EXCEED"),
		@ApiResponse(responseCode = "415", description = "FILE_TYPE_UNSUPPORTED"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/member/profile-photo")
	public ResponseEntity<DefaultResponseDto<Object>> saveMemberPhoto(
		@AuthInfo AuthResponse authResponse,
		@RequestPart(value = "file", required = false) MultipartFile file
	) {
		memberPhotoUseCase.savePhoto(member);
		//        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
		//        Member member = memberService.findById(memberId);
		//
		//        photoService.validateFileExistence(file);
		//        photoService.validateFileType(file);
		//
		//        memberServiceFacade.saveMemberPhoto(member, file);

		return ResponseEntity.status(201)
			.body(DefaultResponseDto.builder()
				.responseCode("PROFILE_PHOTO_SAVED")
				.responseMessage("회원 프로필 사진 저장 완료")
				.build());
	}

	@Operation(summary = "프로필 사진 삭제", description = "- 삭제할 사진이 없다면 404(FILE_NOT_FOUND)를 던집니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "PROFILE_PHOTO_DELETED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "404", description = "FILE_NOT_FOUND"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@DeleteMapping("/member/profile-photo")
	public ResponseEntity<DefaultResponseDto<Object>> deleteMemberPhoto(
		@AuthInfo AuthResponse authResponse
	) {
		memberPhotoUseCase.deletePhoto(member);

		//        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
		//        Member member = memberService.findById(memberId);
		//
		//        MemberPhoto memberPhoto = memberPhotoService.findByMember(member);
		//        if (memberPhoto == null)
		//            throw new CustomException(null, FILE_NOT_FOUND);
		//
		//        memberPhotoService.delete(memberPhoto);

		return ResponseEntity.status(200)
			.body(DefaultResponseDto.builder()
				.responseCode("PROFILE_PHOTO_DELETED")
				.responseMessage("회원 프로필 사진 삭제 완료")
				.build());
	}
}
