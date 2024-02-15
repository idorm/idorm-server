package idorm.idormServer.community.adapter.in.web;

import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.common.dto.DefaultResponseDto;
import idorm.idormServer.community.application.port.in.PostUseCase;
import idorm.idormServer.community.application.port.in.dto.PostResponse;
import idorm.idormServer.community.application.port.in.dto.PostSaveRequest;
import idorm.idormServer.community.application.port.in.dto.PostSummaryResponse;
import idorm.idormServer.community.application.port.in.dto.PostUpdateRequest;
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
public class PostController {

    private final PostUseCase postUseCase;

    @Operation(summary = "기숙사별 홈화면 게시글 목록 조회", description = "- 페이징 적용으로 page는 페이지 번호를 의미합니다.\n " +
            "- page는 0부터 시작하며 서버에서 10개 단위로 페이징해서 반환합니다.\n " +
            "- 서버에서 최신 순으로 정렬하여 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "POST_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = PostSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "DORMCATEGORY_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/posts/{dormitory-category}")
    public ResponseEntity<DefaultResponseDto<Object>> findPostsFilteredByCategory(
            @Login Member member,
            @PathVariable(value = "dormitory-category") String dormCategoryRequest,
            @RequestParam(value = "page") int pageNum
    ) {

        postUseCase.findPostsFilteredByCategory(member, dormCategoryRequest, pageNum);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_MANY_FOUND")
                        .responseMessage("PostJpaEntity 기숙사 필터링 후 게시글 다건 조회 완료")
                        .build()
                );
    }

    @Operation(summary = "기숙사별 인기 게시글 다건 조회", description = "- 서버에서 공감 순으로 정렬 후 최신 순으로 정렬합니다.\n" +
            "- 인기 게시글은 10개 입니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "TOP_POST_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = PostSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "DORMCATEGORY_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/posts/{dormitory-category}/top")
    public ResponseEntity<DefaultResponseDto<Object>> findTopPostsFilteredByCategory(
            @Login Member member,
            @PathVariable("dormitory-category") String dormCategoryRequest
    ) {
        postUseCase.findTopPostsFilteredByCategory(member, dormCategoryRequest);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TOP_POST_MANY_FOUND")
                        .responseMessage("PostJpaEntity 인기 게시글 다건 조회 완료")
                        .build()
                );
    }

    @Operation(summary = "게시글 단건 조회", description = "- 댓글은 과거 순으로 정렬됩니다.\n")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "POST_FOUND",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400", description = "POSTID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/post/{post-id}")
    public ResponseEntity<DefaultResponseDto<Object>> findOnePost(
            @Login Member member,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
            Long postId
    ) {
        postUseCase.findOnePost(member, postId);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_FOUND")
                        .responseMessage("PostJpaEntity 단건 조회 완료")
                        .build()
                );
    }

    @Operation(summary = "게시글 저장", description = "- 첨부 파일이 없다면 null 이 아닌 빈 배열로 보내주세요.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "POST_SAVED",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "DORMCATEGORY_CHARACTER_INVALID / FIELD_REQUIRED / TITLE_LENGTH_INVALID / " +
                            "CONTENT_LENGTH_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "413",
                    description = "FILE_SIZE_EXCEED / FILE_COUNT_EXCEED"),
            @ApiResponse(responseCode = "415", description = "FILE_TYPE_UNSUPPORTED"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/post",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultResponseDto<Object>> savePost(
            @Login Member member,
            @ModelAttribute PostSaveRequest request
    ) {
        postUseCase.save(member, request);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_SAVED")
                        .responseMessage("PostJpaEntity 저장 완료")
                        .build()
                );
    }

    @Operation(summary = "게시글 수정", description = "- 첨부 파일이 없다면 null 이 아닌 빈 배열로 보내주세요.\n" +
            "- 삭제할 게시글 사진(deletePostPhotoIds)이 없다면 404(POSTPHOTO_NOT_FOUND)를 보냅니다.\n" +
            "- 게시글 수정 후 응답 데이터가 필요하다면 게시들 단건 조회 API를 사용해주세요. ")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "POST_UPDATED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / TITLE_LENGTH_INVALID / CONTENT_LENGTH_INVALID /" +
                            "POSTID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_POST"),
            @ApiResponse(responseCode = "404",
                    description = "DELETED_POST / POST_NOT_FOUND / POSTPHOTO_NOT_FOUND"),
            @ApiResponse(responseCode = "413",
                    description = "FILE_SIZE_EXCEED / FILE_COUNT_EXCEED"),
            @ApiResponse(responseCode = "415", description = "FILE_TYPE_UNSUPPORTED"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @PostMapping(value = "/post/{post-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultResponseDto<Object>> updatePost(
            @Login Member member,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
            Long postId,
            @ModelAttribute PostUpdateRequest request
    ) {
        postUseCase.update(member, postId, request);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_UPDATED")
                        .responseMessage("PostJpaEntity 수정 완료")
                        .build()
                );
    }

    @Operation(summary = "내가 쓴 글 목록 조회", description = "- 서버에서 최신 순으로 정렬하여 응답힙니다.\n " +
            "- 이 API의 경우에는 게시글 생성일자가 아닌 수정일자로 정렬합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "POST_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = PostSummaryResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/posts/write")
    public ResponseEntity<DefaultResponseDto<Object>> findPostsByMember(
            @Login Member member
    ) {

        postUseCase.findPostsByMember(member);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_MANY_FOUND")
                        .responseMessage("PostJpaEntity 내가 쓴 글 목록 조회 완료")
                        .build()
                );
    }


    @Operation(summary = "게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "POST_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400", description = "POSTID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_POST"),
            @ApiResponse(responseCode = "404", description = "POST_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @DeleteMapping("/post/{post-id}")
    public ResponseEntity<DefaultResponseDto<Object>> deletePost(
            @Login Member member,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
            Long postId
    ) {

        postUseCase.delete(member, postId);
        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_DELETED")
                        .responseMessage("PostJpaEntity 게시글 삭제 완료")
                        .build()
                );
    }


}
