package idorm.idormServer.community.adapter.in.web;

import javax.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.common.dto.DefaultResponseDto;
import idorm.idormServer.community.application.port.in.LikeUseCase;
import idorm.idormServer.community.application.port.in.dto.PostSummaryResponse;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.support.token.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "5. Community", description = "커뮤니티 api")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class LikeController {

	private final LikeUseCase likeUseCase;

	@Operation(summary = "내가 공감한 게시글 목록 조회", description = "- 서버에서 최신 순으로 정렬하여 응답힙니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "LIKED_POST_MANY_FOUND",
			content = @Content(schema = @Schema(implementation = PostSummaryResponse.class))),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@GetMapping("/posts/like")
	public ResponseEntity<DefaultResponseDto<Object>> findLikedPostsByMember(
		@Login Member member
	) {
		likeUseCase.findLikedPostsByMember(member);

		return ResponseEntity.status(200)
			.body(DefaultResponseDto.builder()
				.responseCode("LIKED_POST_MANY_FOUND")
				.responseMessage("PostJpaEntity 로그인한 멤버가 공감한 게시글 조회 완료")
				.build()
			);
	}

	@Operation(summary = "게시글 공감하기")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "MEMBER_LIKED_POST",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400", description = "POSTID_NEGATIVEORZERO_INVALID"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "404",
			description = "POST_NOT_FOUND / DELETED_POST"),
		@ApiResponse(responseCode = "409",
			description = "DUPLICATE_LIKED / CANNOT_LIKED_SELF / CANNOT_LIKED_POST_BY_DELETED_MEMBER"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@PutMapping("/post/{post-id}/like")
	public ResponseEntity<DefaultResponseDto<Object>> savePostLikes(
		@Login Member member,
		@PathVariable("post-id")
		@Positive(message = "게시글 식별자는 양수만 가능합니다.")
		Long postId
	) {
		likeUseCase.save(member);

		return ResponseEntity.status(200)
			.body(DefaultResponseDto.builder()
				.responseCode("MEMBER_LIKED_POST")
				.responseMessage("PostJpaEntity 게시글 공감 완료")
				.build()
			);
	}

	@Operation(summary = "게시글 공감 취소하기")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "MEMBER_LIKED_POST_CANCELED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400", description = "POSTID_NEGATIVEORZERO_INVALID"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
		@ApiResponse(responseCode = "404",
			description = "POST_NOT_FOUND / DELETED_POST / POSTLIKEDMEMBER_NOT_FOUND"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	})
	@DeleteMapping("/post/{post-id}/like")
	public ResponseEntity<DefaultResponseDto<Object>> deletePostLikes(
		@Login Member member,
		@PathVariable("post-id")
		@Positive(message = "게시글 식별자는 양수만 가능합니다.")
		Long postId
	) {

		likeUseCase.delete(member);

		return ResponseEntity.status(200)
			.body(DefaultResponseDto.builder()
				.responseCode("MEMBER_LIKED_POST_CANCELED")
				.responseMessage("PostJpaEntity 게시글 공감 삭제 완료")
				.build()
			);
	}
}
