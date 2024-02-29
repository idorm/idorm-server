package idorm.idormServer.community.comment.adapter.in.web;

import static idorm.idormServer.community.comment.adapter.out.CommentResponseCode.*;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.adapter.in.api.Auth;
import idorm.idormServer.auth.adapter.in.api.AuthInfo;
import idorm.idormServer.common.response.SuccessResponse;
import idorm.idormServer.community.comment.application.port.in.CommentUseCase;
import idorm.idormServer.community.comment.application.port.in.dto.CommentRequest;
import idorm.idormServer.community.comment.application.port.in.dto.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "8. Comment", description = "댓글 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

	private final CommentUseCase commentUseCase;

	//	@Auth
	@Operation(summary = "댓글/대댓글 저장", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201", description = "COMMENT_SAVED",
			content = @Content(schema = @Schema(implementation = CommentResponse.class))),
		@ApiResponse(responseCode = "400",
			description = "FIELD_REQUIRED / *_NEGATIVEORZERO_INVALID"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER/ NOT_FOUND_POST / ALREADY_DELETED_POST / "
			+ "NOT_FOUND_COMMENT"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value = "/posts/{post-id}/comments")
	public ResponseEntity<SuccessResponse<Object>> save(
//			@AuthInfo
			AuthResponse authResponse,
		@PathVariable("post-id")
		@Positive(message = "게시글 식별자는 양수만 가능합니다.")
		Long postId,
		@RequestBody @Valid CommentRequest request
	) {
		commentUseCase.save(authResponse, postId, request);
		return ResponseEntity.ok().body(SuccessResponse.from(COMMENT_SAVED));
	}

	//	@Auth
	@DeleteMapping(value = "/post/{post-id}/comment/{comment-id}")
	@Operation(summary = "댓글 삭제", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "COMMENT_DELETED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400",
			description = "POSTID_NEGATIVEORZERO_INVALID / COMMENTID_NEGATIVEORZERO_INVALID"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "403", description = "ACCESS_DENIED_COMMENT"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_POST / ALREADY_DELETED_POST / NOT_FOUND_COMMENT / "
			+ "ALREADY_DELETED_COMMENT"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	public ResponseEntity<SuccessResponse<Object>> delete(
//			@AuthInfo
			AuthResponse authResponse,
		@PathVariable("post-id")
		@Positive(message = "게시글 식별자는 양수만 가능합니다.")
		Long postId,
		@PathVariable("comment-id")
		@Positive(message = "댓글 식별자는 양수만 가능합니다.")
		Long commentId
	) {
		commentUseCase.delete(authResponse, postId, commentId);
		return ResponseEntity.ok().body(SuccessResponse.from(COMMENT_DELETED));
	}

	//	@Auth
	@Operation(summary = "내가 작성한 댓글 목록 조회", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "COMMENT_MANY_FOUND",
			content = @Content(schema = @Schema(implementation = CommentResponse.class))),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@GetMapping(value = "/comments/me")
	public ResponseEntity<SuccessResponse<Object>> findCommentsByMember(
//			@AuthInfo
			AuthResponse authResponse
	) {
		List<CommentResponse> responses = commentUseCase.findAllByMember(authResponse);
		return ResponseEntity.ok().body(SuccessResponse.of(COMMENT_MANY_FOUND, responses));
	}
}
