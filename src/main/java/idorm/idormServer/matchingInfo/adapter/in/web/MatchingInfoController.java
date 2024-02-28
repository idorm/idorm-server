package idorm.idormServer.matchingInfo.adapter.in.web;

import static idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode.*;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.auth.adapter.in.api.Auth;
import idorm.idormServer.auth.adapter.in.api.AuthInfo;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.common.response.SuccessResponse;
import idorm.idormServer.matchingInfo.application.port.in.MatchingInfoUseCase;
import idorm.idormServer.matchingInfo.application.port.in.dto.MatchingInfoRequest;
import idorm.idormServer.matchingInfo.application.port.in.dto.MatchingInfoResponse;
import idorm.idormServer.matchingInfo.application.port.in.dto.MatchingInfoVisibilityRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "6. MatchingInfo", description = "온보딩 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matching/me")
public class MatchingInfoController {

	private final MatchingInfoUseCase matchingInfoUseCase;

	@Auth
	@Operation(summary = "온보딩 생성", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201", description = "MATCHINGINFO_SAVED",
			content = @Content(schema = @Schema(implementation = MatchingInfoResponse.class))),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED / INVALID_OPENKAKAOLINK_LENGTH / "
			+ "INVALID_WAKEUPTIME_LENGTH / INVALID_CLEENUP_STATUS_LENGTH / INVALID_SHOWERTIME_LENGTH / "
			+ "INVALID_MBTI_LENGTH / INVALID_MBTI_CHARACTER / INVALID_DORMCATEGORY_CHARACTER / "
			+ "INVALID_JOIN_PERIOD_CHARACTER / INVALID_GENDER_CHARACTER / INVALID_AGE_LENGTH / INVALID_WISHTEXT_LENGTH"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER"),
		@ApiResponse(responseCode = "409", description = "DUPLICATE_MATCHINGINFO"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<SuccessResponse<Object>> save(
		@AuthInfo AuthResponse auth,
		@RequestBody @Valid MatchingInfoRequest request
	) {
		MatchingInfoResponse response = matchingInfoUseCase.save(auth, request);
		return ResponseEntity.status(201).body(SuccessResponse.of(MATCHINGINFO_SAVED, response));
	}

	@Auth
	@Operation(summary = "온보딩 수정", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "MATCHINGINFO_UPDATED",
			content = @Content(schema = @Schema(implementation = MatchingInfoResponse.class))),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED / INVALID_OPENKAKAOLINK_LENGTH / "
			+ "INVALID_WAKEUPTIME_LENGTH / INVALID_CLEENUP_STATUS_LENGTH / INVALID_SHOWERTIME_LENGTH / "
			+ "INVALID_MBTI_LENGTH / INVALID_MBTI_CHARACTER / INVALID_DORMCATEGORY_CHARACTER / "
			+ "INVALID_JOIN_PERIOD_CHARACTER / INVALID_GENDER_CHARACTER / INVALID_AGE_LENGTH / INVALID_WISHTEXT_LENGTH"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_MATCHINGINFO"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@PutMapping
	public ResponseEntity<SuccessResponse<Object>> editAll(
		@AuthInfo AuthResponse auth,
		@RequestBody @Valid MatchingInfoRequest request
	) {
		MatchingInfoResponse response = matchingInfoUseCase.editAll(auth, request);
		return ResponseEntity.ok().body(SuccessResponse.of(MATCHINGINFO_UPDATED, response));
	}

	@Auth
	@Operation(summary = "온보딩 공개 여부 수정", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "ISMATCHINGINFOPUBLIC_UPDATED"),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_MATCHINGINFO"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@PatchMapping
	public ResponseEntity<SuccessResponse<Object>> editVisibility(
		@AuthInfo AuthResponse auth,
		@RequestBody @Valid MatchingInfoVisibilityRequest request
	) {
		matchingInfoUseCase.editVisibility(auth, request);
		return ResponseEntity.ok().body(SuccessResponse.from(ISMATCHINGINFOPUBLIC_UPDATED));
	}

	@Auth
	@Operation(summary = "내 온보딩 조회", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "MATCHINGINFO_FOUND",
			content = @Content(schema = @Schema(implementation = MatchingInfoResponse.class))),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_MATCHINGINFO"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@GetMapping
	public ResponseEntity<SuccessResponse<Object>> getInfo(
		@AuthInfo AuthResponse auth
	) {
		MatchingInfoResponse response = matchingInfoUseCase.getInfo(auth);
		return ResponseEntity.ok().body(SuccessResponse.of(MATCHINGINFO_FOUND, response));
	}

	@Auth
	@Operation(summary = "온보딩 삭제", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "MATCHINGINFO_DELETED"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@DeleteMapping
	public ResponseEntity<SuccessResponse<Object>> delete(
		@AuthInfo AuthResponse auth
	) {
		matchingInfoUseCase.delete(auth);
		return ResponseEntity.ok().body(SuccessResponse.from(MATCHINGINFO_DELETED));
	}
}
