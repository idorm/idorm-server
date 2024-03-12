package idorm.idormServer.community.post.adapter.in.web;

import static idorm.idormServer.community.post.adapter.out.PostResponseCode.MAIN_POST_MANY_FOUND;
import static idorm.idormServer.community.post.adapter.out.PostResponseCode.MY_POST_MANY_FOUND;
import static idorm.idormServer.community.post.adapter.out.PostResponseCode.POST_DELETED;
import static idorm.idormServer.community.post.adapter.out.PostResponseCode.POST_FOUND;
import static idorm.idormServer.community.post.adapter.out.PostResponseCode.POST_SAVED;
import static idorm.idormServer.community.post.adapter.out.PostResponseCode.POST_UPDATED;
import static idorm.idormServer.community.post.adapter.out.PostResponseCode.TOP_POST_MANY_FOUND;

import idorm.idormServer.auth.adapter.in.web.Auth;
import idorm.idormServer.auth.adapter.in.web.AuthInfo;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.common.response.SuccessResponse;
import idorm.idormServer.community.comment.adapter.out.exception.InvalidCommentContentLengthException;
import idorm.idormServer.community.post.adapter.out.exception.InvalidPostTitleLengthException;
import idorm.idormServer.community.post.application.port.in.PostUseCase;
import idorm.idormServer.community.post.application.port.in.dto.PostListResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostSaveRequest;
import idorm.idormServer.community.post.application.port.in.dto.PostUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "6. Post", description = "게시글 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostController {

  private final PostUseCase postUseCase;

  @Auth
  @Operation(summary = "게시글 저장", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "POST_SAVED",
          content = @Content(schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "400",
          description = "INVALID_DORMCATEGORY_CHARACTER / FIELD_REQUIRED / INVALID_TITLE_LENGTH / "
              + "INVALID_CONTENT_LENGTH_"),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "413", description = "EXCEED_FILE_SIZE / EXCEED_FILE_COUNT"),
      @ApiResponse(responseCode = "415", description = "UNSUPPORTED_FILE_TYPE"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping(value = "/posts/new",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SuccessResponse<Object>> savePost(
      @AuthInfo AuthResponse authResponse,
      @ModelAttribute("request") @Valid PostSaveRequest request, BindingResult result
  ) {
    validateModelAttribute(result);
    PostResponse response = postUseCase.save(authResponse, request);
    return ResponseEntity.ok().body(SuccessResponse.of(POST_SAVED, response));
  }

  @Auth
  @Operation(summary = "게시글 수정", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "POST_UPDATED",
          content = @Content(schema = @Schema(implementation = Object.class))),
      @ApiResponse(responseCode = "400", description =
          "FIELD_REQUIRED / INVALID_TITLE_LENGTH / INVALID_CONTENT_LENGTH / "
              + "INVALID_*_LENGTH"),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_POST"),
      @ApiResponse(responseCode = "404", description = "ALREADY_DELETED_POST / NOT_FOUND_POST / NOT_FOUND_POSTPHOTO"),
      @ApiResponse(responseCode = "413", description = "EXCEED_FILE_SIZE / EXCEED_FILE_COUNT"),
      @ApiResponse(responseCode = "415", description = "UNSUPPORTED_FILE_TYPE"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @PostMapping(value = "/posts/{post-id}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SuccessResponse<Object>> updatePost(
      @AuthInfo AuthResponse authResponse,
      @PathVariable("post-id")
      @Positive(message = "게시글 식별자는 양수만 가능합니다.")
      Long postId,
      @ModelAttribute @Valid PostUpdateRequest request, BindingResult result
  ) {
    validateModelAttribute(result);
    postUseCase.update(authResponse, postId, request);
    return ResponseEntity.ok().body(SuccessResponse.from(POST_UPDATED));
  }

  @Auth
  @Operation(summary = "게시글 삭제", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "POST_DELETED",
          content = @Content(schema = @Schema(implementation = Object.class))),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_POST"),
      @ApiResponse(responseCode = "404", description = "NOT_FOUND_POST"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @DeleteMapping("/posts/{post-id}")
  public ResponseEntity<SuccessResponse<Object>> deletePost(
      @AuthInfo AuthResponse authResponse,
      @PathVariable("post-id")
      @Positive(message = "게시글 식별자는 양수만 가능합니다.")
      Long postId
  ) {
    postUseCase.delete(authResponse, postId);
    return ResponseEntity.ok().body(SuccessResponse.from(POST_DELETED));
  }

  @Auth
  @Operation(summary = "내가 쓴 글 목록 조회", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "POST_MANY_FOUND",
          content = @Content(schema = @Schema(implementation = PostListResponse.class))),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @GetMapping("/posts/me")
  public ResponseEntity<SuccessResponse<Object>> findPostsByMember(
      @AuthInfo AuthResponse authResponse
  ) {
    List<PostListResponse> responses = postUseCase.findPostsByMember(authResponse);
    return ResponseEntity.ok().body(SuccessResponse.of(MY_POST_MANY_FOUND, responses));
  }

  @Auth
  @Operation(summary = "기숙사별 홈화면 게시글 목록 조회", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "POST_MANY_FOUND",
          content = @Content(schema = @Schema(implementation = PostListResponse.class))),
      @ApiResponse(responseCode = "400", description = "INVALID_DORMCATEGORY_CHARACTER"),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @GetMapping("/posts/{dormitory-category}")
  public ResponseEntity<SuccessResponse<Object>> findPostsByDormCategory(
      @PathVariable(value = "dormitory-category") String dormCategoryRequest,
      @RequestParam(value = "page") int pageNum
  ) {
    Page<PostListResponse> responses = postUseCase.findPostsByDormCategory(dormCategoryRequest, pageNum);
    return ResponseEntity.ok().body(SuccessResponse.of(MAIN_POST_MANY_FOUND, responses));
  }

  @Auth
  @Operation(summary = "기숙사별 인기 게시글 다건 조회", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "TOP_POST_MANY_FOUND",
          content = @Content(schema = @Schema(implementation = PostListResponse.class))),
      @ApiResponse(responseCode = "400", description = "INVALID_DORMCATEGORY_CHARACTER"),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @GetMapping("/posts/{dormitory-category}/top")
  public ResponseEntity<SuccessResponse<Object>> findTopPostsFilteredByCategory(
      @PathVariable("dormitory-category") String dormCategoryRequest
  ) {
    List<PostListResponse> responses = postUseCase.findTopPostsByDormCategory(dormCategoryRequest);
    return ResponseEntity.ok().body(SuccessResponse.of(TOP_POST_MANY_FOUND, responses));
  }

  @Auth
  @Operation(summary = "게시글 단건 조회", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "POST_FOUND",
          content = @Content(schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "404", description = "NOT_FOUND_POST / ALREADY_DELETED_POST"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @GetMapping("/posts/{post-id}")
  public ResponseEntity<SuccessResponse<Object>> findOnePost(
      @AuthInfo AuthResponse authResponse,
      @PathVariable("post-id")
      @Positive(message = "게시글 식별자는 양수만 가능합니다.")
      Long postId
  ) {
    PostResponse response = postUseCase.findOneByPostId(authResponse, postId);
    return ResponseEntity.ok().body(SuccessResponse.of(POST_FOUND, response));
  }

  private void validateModelAttribute(BindingResult bindingResult) {
    String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
    if (message.contains("title")) {
      throw new InvalidPostTitleLengthException();
    } else if (message.contains("content")) {
      throw new InvalidCommentContentLengthException();
    }
  }
}