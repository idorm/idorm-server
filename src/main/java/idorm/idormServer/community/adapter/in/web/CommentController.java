package idorm.idormServer.community.adapter.in.web;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.common.dto.DefaultResponseDto;
import idorm.idormServer.community.application.port.in.CommentUseCase;
import idorm.idormServer.community.application.port.in.dto.CommentRequest;
import idorm.idormServer.community.application.port.in.dto.CommentResponse;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.support.token.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "5. Comment", description = "댓글 api")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class CommentController {

	private final CommentUseCase commentUseCase;

	@Operation(summary = "[FCM 적용] 댓글/대댓글 저장",
		description = "- 대댓글인 경우, parentCommentId(부모 댓글 식별자)가 게시글에 존재하지 않는다면 404(COMMENT_NOT_FOUND)를 반환합니다.\n" +
			"- 댓글이 저장되면 게시글 작성자에게 FCM 알람을 보냅니다.\n" +
			"- 대댓글이 저장되면 게시글 작성자, 부모 댓글 작성자, 부모 댓글에 단 대댓글들의 작성자들 에게 FCM 알람을 보냅니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201", description = "COMMENT_SAVED",
			content = @Content(schema = @Schema(implementation = CommentResponse.class))),
		@ApiResponse(responseCode = "400",
			description = "FIELD_REQUIRED / *_NEGATIVEORZERO_INVALID"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "404",
			description = "POST_NOT_FOUND / DELETED_POST / COMMENT_NOT_FOUND"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value = "/post/{post-id}/comment")
	public ResponseEntity<DefaultResponseDto<Object>> saveComment(
		@Login Member member,
		@PathVariable("post-id")
		@Positive(message = "게시글 식별자는 양수만 가능합니다.")
		Long postId,
		@RequestBody @Valid CommentRequest request
	) {

		commentUseCase.save(member, postId, request);

		return ResponseEntity.status(201)
			.body(DefaultResponseDto.builder()
				.responseCode("COMMENT_SAVED")
				.responseMessage("CommentJpaEntity 댓글 저장 완료")
				.build()
			);
	}

	@DeleteMapping(value = "/post/{post-id}/comment/{comment-id}")
	@Operation(summary = "댓글 삭제")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "COMMENT_DELETED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400",
			description = "POSTID_NEGATIVEORZERO_INVALID / COMMENTID_NEGATIVEORZERO_INVALID"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "403", description = "ACCESS_DENIED_COMMENT"),
		@ApiResponse(responseCode = "404",
			description = "POST_NOT_FOUND / DELETED_POST / COMMENT_NOT_FOUND / DELETED_COMMENT"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	public ResponseEntity<DefaultResponseDto<Object>> deleteComment(
		@Login Member member,
		@PathVariable("post-id")
		@Positive(message = "게시글 식별자는 양수만 가능합니다.")
		Long postId,
		@PathVariable("comment-id")
		@Positive(message = "댓글 식별자는 양수만 가능합니다.")
		Long commentId
	) {

		commentUseCase.delete(member, postId, commentId);

		return ResponseEntity.status(200)
			.body(DefaultResponseDto.builder()
				.responseCode("COMMENT_DELETED")
				.responseMessage("CommentJpaEntity 삭제 완료")
				.build()
			);
	}

	@Operation(summary = "내가 작성한 댓글 목록 조회", description = "- 서버에서 최신 순으로 정렬하여 응답합니다. \n" +
		"- 댓글은 수정 기능을 제공하지 않으므로 생성일자로 정렬합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "COMMENT_MANY_FOUND",
			content = @Content(schema = @Schema(implementation = CommentResponse.class))),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@GetMapping(value = "/comments")
	public ResponseEntity<DefaultResponseDto<Object>> findCommentsByMember(
		@Login Member member
	) {

		commentUseCase.findCommentsByMember(member);

		return ResponseEntity.status(200)
			.body(DefaultResponseDto.builder()
				.responseCode("COMMENT_MANY_FOUND")
				.responseMessage("CommentJpaEntity 로그인한 멤버가 작성한 모든 댓글 조회 완료")
				.build()
			);
	}
}
