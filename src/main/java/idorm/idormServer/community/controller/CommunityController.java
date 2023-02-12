package idorm.idormServer.community.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.dto.comment.CommentParentResponseDto;
import idorm.idormServer.community.dto.comment.CommentDefaultRequestDto;
import idorm.idormServer.community.dto.comment.CommentDefaultResponseDto;
import idorm.idormServer.community.dto.post.PostDefaultResponseDto;
import idorm.idormServer.community.dto.post.PostOneResponseDto;
import idorm.idormServer.community.service.CommentService;
import idorm.idormServer.community.service.PostLikedMemberService;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.community.dto.post.PostSaveRequestDto;
import idorm.idormServer.community.dto.post.PostUpdateRequestDto;
import idorm.idormServer.exception.CustomException;

import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.exception.ExceptionCode.*;

@Api(tags = "커뮤니티")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class CommunityController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final PostService postService;
    private final PostLikedMemberService postLikedMemberService;
    private final CommentService commentService;

    @ApiOperation(value = "기숙사별 홈화면 게시글 목록 조회", notes = "- 페이징 적용으로 page는 페이지 번호를 의미합니다.\n " +
            "- page는 0부터 시작하며 서버에서 20개 단위로 페이징해서 반환합니다.\n " +
            "- 서버에서 최신순으로 정렬하여 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "POST_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = PostDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "DORMCATEGORY_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @GetMapping("/posts/{dormitory-category}")
    public ResponseEntity<DefaultResponseDto<Object>> findPostsFilteredByCategory(
            @PathVariable(value = "dormitory-category") String dormCategoryRequest,
            @RequestParam(value = "page") int pageNum
    ) {
        DormCategory dormCategory = DormCategory.validateType(dormCategoryRequest);

        Page<Post> posts = postService.findManyPostsByDormCategory(dormCategory, pageNum);

        List<PostDefaultResponseDto> response = posts.stream()
                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_MANY_FOUND")
                        .responseMessage("Post 기숙사 필터링 후 게시글 다건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "기숙사별 인기 게시글 다건 조회", notes = "- 서버에서 최신순으로 정렬하여 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "TOP_POST_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = PostDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "DORMCATEGORY_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @GetMapping("/posts/{dormitory-category}/top")
    public ResponseEntity<DefaultResponseDto<Object>> findTopPostsFilteredByCategory(
            @PathVariable("dormitory-category") String dormCategoryRequest
    ) {
        DormCategory dormCategory = DormCategory.validateType(dormCategoryRequest);

        List<Post> posts = postService.findTopPosts(dormCategory);

        List<PostDefaultResponseDto> response = posts.stream()
                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TOP_POST_MANY_FOUND")
                        .responseMessage("Post 인기 게시글 다건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "게시글 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = PostOneResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @GetMapping("/post/{post-id}")
    public ResponseEntity<DefaultResponseDto<Object>> findOnePost(
            @PathVariable("post-id") Long postId
    ) {

        Post foundPost = postService.findById(postId);

        List<Comment> foundComments = commentService.findCommentsByPostId(postId);
        List<CommentParentResponseDto> customResponseDtos = new ArrayList<>();
        for(Comment comment : foundComments) {
            // 부모 식별자를 가지고 있지 않다면, 해당 부모 식별자를 가지고 있는 댓글 리스트와 함께 dto 생성
            if(comment.getParentCommentId() == null) { // 대댓글이 아닌 댓글임
                List<Comment> foundSubComments =
                        commentService.findSubCommentsByParentCommentId(postId, comment.getId());

                List<CommentDefaultResponseDto> defaultResponseDtos = new ArrayList<>();
                for (Comment subComment : foundSubComments) {
                    CommentDefaultResponseDto commentDefaultResponseDto = new CommentDefaultResponseDto(subComment);
                    defaultResponseDtos.add(commentDefaultResponseDto);
                }

                CommentParentResponseDto customResponseDto = new CommentParentResponseDto(comment, defaultResponseDtos);
                customResponseDtos.add(customResponseDto);
            }
        }

        PostOneResponseDto response = new PostOneResponseDto(foundPost);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 단건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "게시글 저장")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED",
                    content = @Content(schema = @Schema(implementation = PostOneResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "DORMCATEGORY_CHARACTER_INVALID / FIELD_REQUIRED"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "413",
                    description = "FILE_SIZE_EXCEED / FILE_COUNT_EXCEED"),
            @ApiResponse(responseCode = "415",
                    description = "FILE_TYPE_UNSUPPORTED"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/post",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultResponseDto<Object>> savePost(
            HttpServletRequest request,
            @ModelAttribute @Valid PostSaveRequestDto postRequest
    ) {

        DormCategory dormCategory = DormCategory.validateType(postRequest.getDormCategory());

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(postRequest.getFiles().size() > 10) {
            throw new CustomException(FILE_COUNT_EXCEED);
        }

        Post createdPost = postService.savePost(
                member,
                postRequest.getFiles(),
                dormCategory,
                postRequest.getTitle(),
                postRequest.getContent(),
                postRequest.isAnonymous());

        PostOneResponseDto response = new PostOneResponseDto(createdPost);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("CREATED")
                        .responseMessage("Post 저장 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "게시글 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "POST_UPDATED",
                    content = @Content(schema = @Schema(implementation = PostOneResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "DELETED_POST / POST_NOT_FOUND"),
            @ApiResponse(responseCode = "413",
                    description = "FILE_SIZE_EXCEED / FILE_COUNT_EXCEED"),
            @ApiResponse(responseCode = "415",
                    description = "FILE_TYPE_UNSUPPORTED"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @PostMapping(value = "/post/{post-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultResponseDto<Object>> updatePost(
            HttpServletRequest request,
            @PathVariable("post-id") Long updatePostId,
            @ModelAttribute @Valid PostUpdateRequestDto updateRequest
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        postService.validatePostPhotoCountExceeded(updateRequest.getFiles().size());

        Post foundPost = postService.findById(updatePostId);
        postService.validatePostAuthorization(foundPost, member);

        postService.updatePost(updatePostId,
                updateRequest.getTitle(),
                updateRequest.getContent(),
                updateRequest.isAnonymous(),
                updateRequest.getFiles());

        PostOneResponseDto response = new PostOneResponseDto(foundPost);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_UPDATED")
                        .responseMessage("Post 수정 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "내가 쓴 글 목록 조회", notes = "- 서버에서 최신순으로 정렬하여 응답힙니다.\n " +
            "- 이 API의 경우에는 게시글 생성일자가 아닌 수정일자로 정렬합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "POST_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = PostDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @GetMapping("/posts/write")
    public ResponseEntity<DefaultResponseDto<Object>> findPostsByMember(
            HttpServletRequest request2
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        List<Post> posts = postService.findPostsByMember(member);

        List<PostDefaultResponseDto> response = posts.stream()
                .map(post -> new PostDefaultResponseDto(post))
                .collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_MANY_FOUND")
                        .responseMessage("Post 내가 쓴 글 목록 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "내가 공감한 게시글 목록 조회", notes = "서버에서 최신순으로 정렬하여 응답힙니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "LIKED_POST_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = PostDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @GetMapping("/posts/like")
    public ResponseEntity<DefaultResponseDto<Object>> findLikedPostsByMember(
            HttpServletRequest request2
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        memberService.findById(loginMemberId);

        List<Long> foundPostIds = postLikedMemberService.findLikedPostIdsByMemberId(loginMemberId);

        List<Post> resultPosts = new ArrayList<>();

        for(Long postId : foundPostIds) {
            Post post = postService.findById(postId);
            resultPosts.add(post);
        }

        List<PostDefaultResponseDto> response = resultPosts.stream()
                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("LIKED_POST_MANY_FOUND")
                        .responseMessage("Post 로그인한 멤버가 공감한 게시글 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "게시글 공감하기")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "MEMBER_LIKED_POST",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST"),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_LIKED / CANNOT_LIKED_SELF"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @PutMapping("/post/{post-id}/like")
    public ResponseEntity<DefaultResponseDto<Object>> savePostLikes(
            HttpServletRequest request2, @PathVariable("post-id") Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        postLikedMemberService.savePostLikedMember(member, post);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_LIKED_POST")
                        .responseMessage("Post 게시글 공감 완료")
                        .build()
                );
    }

    @ApiOperation(value = "게시글 공감 취소하기")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "MEMBER_LIKED_POST_CANCELED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST / POSTLIKEDMEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @DeleteMapping("/post/{post-id}/like")
    public ResponseEntity<DefaultResponseDto<Object>> deletePostLikes(
            HttpServletRequest request2, @PathVariable("post-id") Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        postLikedMemberService.deletePostLikedMember(member, post);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_LIKED_POST_CANCELED")
                        .responseMessage("Post 게시글 공감 삭제 완료")
                        .build()
                );
    }

    @ApiOperation(value = "게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "POST_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER / UNAUTHORIZED_POST"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @DeleteMapping("/post/{post-id}")
    public ResponseEntity<DefaultResponseDto<Object>> deletePost(
            HttpServletRequest request2, @PathVariable("post-id") Long postId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        Post deletePost = postService.findById(postId);
        postService.validatePostAuthorization(deletePost, member);
        postService.deletePost(deletePost);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_DELETED")
                        .responseMessage("Post 게시글 삭제 완료")
                        .build()
                );
    }

    /**
     * Comment
     */
    @ApiOperation(value = "댓글/대댓글 저장",
            notes = "- 대댓글인 경우, 부모 댓글이 조회되지 않는다면 COMMENT_NOT_FOUND(404)를 반환합니다.\n" +
                    "- 이 때, 부모 댓글이 삭제된 경우도 DB에 저장되어있으므로 문제 없습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "COMMENT_SAVED",
                    content = @Content(schema = @Schema(implementation = CommentDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST / COMMENT_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/post/{post-id}/comment")
    public ResponseEntity<DefaultResponseDto<Object>> saveComment(
            HttpServletRequest request,
            @PathVariable("post-id") Long postId,
            @RequestBody @Valid CommentDefaultRequestDto requestDto
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        if(requestDto.getParentCommentId() != null) {
            commentService.isExistParentCommentFromPost(postId, requestDto.getParentCommentId());
        }

        Comment createdComment = commentService.saveComment(
                requestDto.getContent(),
                requestDto.isAnonymous(),
                post,
                loginMember);

        if(requestDto.getParentCommentId() != null) {
            commentService.saveParentCommentId(requestDto.getParentCommentId(), createdComment);
        }

        CommentDefaultResponseDto response = new CommentDefaultResponseDto(createdComment);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("COMMENT_SAVED")
                        .responseMessage("Comment 댓글 저장 완료")
                        .data(response)
                        .build()
                );
    }

    @DeleteMapping(value = "/post/{post-id}/comment/{comment-id}")
    @ApiOperation(value = "댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "COMMENT_DELETED"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER / UNAUTHORIZED_DELETE"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST / COMMENT_NOT_FOUND / DELETED_COMMENT"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> deleteComment(
            HttpServletRequest request,
            @PathVariable("post-id") Long postId,
            @PathVariable("comment-id") Long commentId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);
        postService.findById(postId);
        commentService.deleteComment(commentId, loginMember);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("COMMENT_DELETED")
                        .responseMessage("Comment 삭제 완료")
                        .build()
                );
    }

    @ApiOperation(value = "내가 작성한 댓글 목록 조회", notes = "서버에서 최신순으로 정렬하여 응답합니다. 이 API의 경우에는 댓글 생성일자가 아닌 " +
            "수정일자로 정렬합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "COMMENT_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = CommentDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @GetMapping(value = "/comments")
    public ResponseEntity<DefaultResponseDto<Object>> findCommentsByMember(
            HttpServletRequest request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        List<Comment> foundComments = commentService.findCommentsByMember(loginMember);

        List<CommentDefaultResponseDto> response = foundComments.stream()
                .map(comment -> new CommentDefaultResponseDto(comment)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("COMMENT_MANY_FOUND")
                        .responseMessage("Comment 로그인한 멤버가 작성한 모든 댓글 조회 완료")
                        .data(response)
                        .build()
                );
    }
}
