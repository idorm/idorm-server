package idorm.idormServer.matchingMate.adapter.in.web;

import static idorm.idormServer.matchingMate.adapter.out.MatchingMateResponseCode.*;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.auth.adapter.in.api.Auth;
import idorm.idormServer.auth.adapter.in.api.AuthInfo;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.common.response.SuccessResponse;
import idorm.idormServer.matchingMate.application.port.in.MatchingMateUseCase;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.application.port.in.dto.PreferenceMateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "5. Matching Mate", description = "룸메이트 매칭 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matching/mates")
public class MatchingMateController {

	private final MatchingMateUseCase matchingMateUseCase;

	@Auth
	@Operation(summary = "좋아요한 메이트들 조회", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "LIKEDMEMBERS_FOUND",
			content = @Content(schema = @Schema(implementation = MatchingMateResponse.class))),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_MATCHINGINFO"),
		@ApiResponse(responseCode = "409", description = "ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@GetMapping("/favorite")
	public ResponseEntity<SuccessResponse<Object>> findFavoriteMates(
		@AuthInfo AuthResponse auth
	) {
		return ResponseEntity.ok()
			.body(SuccessResponse.of(LIKEDMEMBERS_FOUND, matchingMateUseCase.findFavoriteMates(auth)));
	}

	@Auth
	@Operation(summary = "싫어요한 메이트들 조회", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "DISLIKEDMEMBERS_FOUND",
			content = @Content(schema = @Schema(implementation = MatchingMateResponse.class))),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_MATCHINGINFO"),
		@ApiResponse(responseCode = "409", description = "ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@GetMapping("/nonfavorite")
	public ResponseEntity<SuccessResponse<Object>> findNonFavoriteMates(
		@AuthInfo AuthResponse auth
	) {
		return ResponseEntity.ok().body(SuccessResponse.of(DISLIKEDMEMBERS_FOUND,
			matchingMateUseCase.findNonFavoriteMates(auth)));
	}

	@Auth
	@Operation(summary = "호﹒불호 메이트 추가", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "LIKEDMEMBER_SAVED / DISLIKEDMEMBER_SAVED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED / TARGETMEMBERID_NEGATIVEORZERO_INVALID / "
			+ "INVALID_MATE_PREFERENCE_CHARACTER / INVALID_TARGET_SELF"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_MATCHINGINFO"),
		@ApiResponse(responseCode = "409", description = "ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC / "
			+ "DUPLICATED_LIKED_MEMBER / DUPLICATED_DISLIKED_MEMBER"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@PostMapping
	public ResponseEntity<SuccessResponse<Object>> addFavoriteOrNonFavoriteMate(
		@AuthInfo AuthResponse auth,
		@RequestBody @Valid PreferenceMateRequest request
	) {
		matchingMateUseCase.addMate(auth, request);

		if (request.getPreferenceType().isFavorite()) {
			return ResponseEntity.ok().body(SuccessResponse.from(LIKED_MEMBER_SAVED));
		} else {
			return ResponseEntity.ok().body(SuccessResponse.from(DISLIKED_MEMBER_SAVED));
		}
	}

	@Auth
	@Operation(summary = "호﹒불호 메이트 삭제", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "LIKEDMEMBER_DELETED / DISLIKEDMEMBER_DELETED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED / TARGETMEMBERID_NEGATIVEORZERO_INVALID / "
			+ "INVALID_MATE_PREFERENCE_CHARACTER "),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@DeleteMapping
	public ResponseEntity<SuccessResponse<Object>> deleteFavoriteOrNonFavoriteMate(
		@AuthInfo AuthResponse auth,
		@RequestBody @Valid PreferenceMateRequest request
	) {
		matchingMateUseCase.deleteMate(auth, request);

		if (request.getPreferenceType().isFavorite()) {
			return ResponseEntity.ok().body(SuccessResponse.from(LIKED_MEMBER_DELETED));
		} else {
			return ResponseEntity.ok().body(SuccessResponse.from(DISLIKED_MEMBER_DELETED));
		}
	}

	@Auth
	@Operation(summary = "(기본) 메이트들 조회", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "MATCHING_MEMBERS_FOUND",
			content = @Content(schema = @Schema(implementation = MatchingMateResponse.class))),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_MATCHINGINFO"),
		@ApiResponse(responseCode = "409", description = "ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@GetMapping
	public ResponseEntity<SuccessResponse<Object>> findMates(
		@AuthInfo AuthResponse auth
	) {
		return ResponseEntity.ok().body(SuccessResponse.of(MATCHING_MEMBERS_FOUND,
			matchingMateUseCase.findMates(auth)));
	}

	@Auth
	@Operation(summary = "(필터링) 메이트들 조회", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "FILTERED_MATCHING_MEMBERS_FOUND",
			content = @Content(schema = @Schema(implementation = MatchingMateResponse.class))),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED / INVALID_DORMCATEGORY_CHARACTER / "
			+ "INVALID_JOIN_PERIOD_CHARACTER / INVALID_AGE_LENGTH"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_MATCHINGINFO"),
		@ApiResponse(responseCode = "409", description = "ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@PostMapping("/filter")
	public ResponseEntity<SuccessResponse<Object>> findFilteredMates(
		@AuthInfo AuthResponse auth,
		@RequestBody @Valid MatchingMateFilterRequest request
	) {
		return ResponseEntity.ok().body(SuccessResponse.of(FILTERED_MATCHING_MEMBERS_FOUND,
			matchingMateUseCase.findFilteredMates(auth, request)));
	}
}