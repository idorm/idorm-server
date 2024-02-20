package idorm.idormServer.community.postLike.adapter.in.web;

import static idorm.idormServer.community.exception.CommunityResponseCode.*;

import java.util.List;

import javax.validation.constraints.Positive;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.domain.Auth;
import idorm.idormServer.auth.domain.AuthInfo;
import idorm.idormServer.common.response.SuccessResponse;
import idorm.idormServer.community.postLike.application.port.in.LikeUseCase;
import idorm.idormServer.community.post.application.port.in.dto.PostListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "5. Community", description = "커뮤니티 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostLikeController {

  private final LikeUseCase likeUseCase;

  @Auth
  @Operation(summary = "게시글 공감하기", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "MEMBER_LIKED_POST",
          content = @Content(schema = @Schema(implementation = Object.class))),
      @ApiResponse(responseCode = "400", description = "POSTID_NEGATIVEORZERO_INVALID"),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
      @ApiResponse(responseCode = "404", description = "POST_NOT_FOUND / DELETED_POST"),
      @ApiResponse(responseCode = "409",
          description = "DUPLICATE_LIKED / CANNOT_LIKED_SELF / CANNOT_LIKED_POST_BY_DELETED_MEMBER"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @PutMapping("/post/{post-id}/like")
  public ResponseEntity<SuccessResponse<Object>> savePostLikes(
      @AuthInfo AuthResponse authResponse,
      @PathVariable("post-id")
      @Positive(message = "게시글 식별자는 양수만 가능합니다.")
      Long postId
  ) {
    likeUseCase.save(authResponse, postId);
    return ResponseEntity.ok().body(SuccessResponse.from(MEMBER_LIKED_POST));
  }

  @Auth
  @Operation(summary = "게시글 공감 취소하기", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
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
  public ResponseEntity<SuccessResponse<Object>> deletePostLikes(
      @AuthInfo AuthResponse authResponse,
      @PathVariable("post-id")
      @Positive(message = "게시글 식별자는 양수만 가능합니다.")
      Long postId
  ) {
    likeUseCase.delete(authResponse, postId);
    return ResponseEntity.ok().body(SuccessResponse.from(MEMBER_LIKED_POST_CANCELED));
  }

  @Auth
  @Operation(summary = "내가 공감한 게시글 목록 조회", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "LIKED_POST_MANY_FOUND",
          content = @Content(schema = @Schema(implementation = PostListResponse.class))),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @GetMapping("/posts/likes/me")
  public ResponseEntity<SuccessResponse<Object>> findLikedPostsByMember(
      @AuthInfo AuthResponse authResponse
  ) {
    List<PostListResponse> responses = likeUseCase.findLikedPostsByMember(authResponse);
    return ResponseEntity.ok().body(SuccessResponse.of(LIKED_POST_MANY_FOUND, responses));
  }
}
