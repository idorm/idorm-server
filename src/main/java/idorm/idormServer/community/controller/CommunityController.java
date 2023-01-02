package idorm.idormServer.community.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.dto.comment.CommentCustomResponseDto;
import idorm.idormServer.community.dto.comment.CommentDefaultRequestDto;
import idorm.idormServer.community.dto.comment.CommentDefaultResponseDto;
import idorm.idormServer.community.dto.post.PostDefaultResponseDto;
import idorm.idormServer.community.dto.post.PostOneResponseDto;
import idorm.idormServer.community.service.CommentService;
import idorm.idormServer.community.service.PostLikedMemberService;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.community.vo.post.SavePostVo;
import idorm.idormServer.community.vo.post.UpdatePostVo;
import idorm.idormServer.exceptions.CustomException;
import idorm.idormServer.exceptions.ErrorResponse;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.exceptions.ErrorCode.*;

@RequiredArgsConstructor
@RestController
@Api(tags = "Community API")
public class CommunityController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final PostService postService;
    private final PostLikedMemberService postLikedMemberService;
    private final CommentService commentService;

    @ApiOperation(value = "기숙사별 홈화면 게시글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = PostDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "DORM_CATEGORY_FORMAT_INVALID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/member/posts/{dormitory-category}")
    public ResponseEntity<DefaultResponseDto<Object>> findPostsFilteredByCategory(
            @PathVariable(value = "dormitory-category") String dormNum
    ) {

        validateDormCategory(dormNum);

        Page<Post> posts = postService.findManyPostsByDormCategory(dormNum);

        List<PostDefaultResponseDto> response = posts.stream()
                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 커뮤니티 홈화면 게시글 목록 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "기숙사별 인기 게시글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = PostDefaultResponseDto.class))),
            @ApiResponse(
                    responseCode = "204",
                    description = "NO CONTENT"),
            @ApiResponse(responseCode = "400",
                    description = "DORM_CATEGORY_FORMAT_INVALID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/member/posts/{dormitory-category}/top")
    public ResponseEntity<DefaultResponseDto<Object>> findTopPostsFilteredByCategory(
            @PathVariable("dormitory-category") String dormNum
    ) {

        validateDormCategory(dormNum);

        List<Post> posts = postService.findTopPosts(dormNum);

        if (posts.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("인기 게시글이 없습니다.")
                            .build());
        }

        List<PostDefaultResponseDto> response = posts.stream()
                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 인기 게시글 목록 조회 완료")
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
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/member/post/{post-id}")
    public ResponseEntity<DefaultResponseDto<Object>> findOnePost(
            @PathVariable("post-id") Long postId
    ) {

        Post foundPost = postService.findById(postId);
        validateDeletedPost(foundPost);

        List<Comment> foundComments = commentService.findCommentsByPostId(postId);
        List<CommentCustomResponseDto> customResponseDtos = new ArrayList<>();
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

                CommentCustomResponseDto customResponseDto = new CommentCustomResponseDto(comment, defaultResponseDtos);
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
                    description = "FILE_SIZE_EXCEEDED / FILE_COUNT_EXCEEDED / DORM_CATEGORY_FORMAT_INVALID /" +
                            " FIELD_REQUIRED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping(value = "/member/post",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultResponseDto<Object>> savePost(
            HttpServletRequest request,
            @ModelAttribute @Valid SavePostVo postRequest
    ) {

        validateDormCategory(postRequest.getDormNum());

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(postRequest.getFiles().size() > 10) {
            throw new CustomException(FILE_COUNT_EXCEEDED);
        }

        Post createdPost = postService.savePost(
                member,
                postRequest.getFiles(),
                postRequest.getDormNum(),
                postRequest.getTitle(),
                postRequest.getContent(),
                postRequest.getIsAnonymous());

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
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = PostOneResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FILE_SIZE_EXCEEDED / FILE_COUNT_EXCEEDED / FIELD_REQUIRED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "DELETED_POST / POST_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping(value = "/member/post/{post-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultResponseDto<Object>> updatePost(
            HttpServletRequest request,
            @PathVariable("post-id") Long updatePostId,
            @ModelAttribute @Valid UpdatePostVo updateRequest
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(updateRequest.getFiles().size() > 10) {
            throw new CustomException(FILE_COUNT_EXCEEDED);
        }

        Post foundPost = postService.findById(updatePostId);
        validateDeletedPost(foundPost);

        postService.updatePost(updatePostId,
                member,
                updateRequest.getTitle(),
                updateRequest.getContent(),
                updateRequest.getIsAnonymous(),
                updateRequest.getFiles());

        PostOneResponseDto response = new PostOneResponseDto(foundPost);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 수정 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "내가 쓴 글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = PostDefaultResponseDto.class))),
            @ApiResponse(
                    responseCode = "204",
                    description = "NO CONTENT"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/member/posts/write")
    public ResponseEntity<DefaultResponseDto<Object>> findPostsByMember(
            HttpServletRequest request2
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        List<Post> posts = postService.findPostsByMember(member);

        if(posts.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("작성한 게시글이 없습니다.")
                            .build());
        }

        List<PostDefaultResponseDto> response = posts.stream()
                .map(post -> new PostDefaultResponseDto(post, post.getMember().getNickname()))
                .collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 내가 쓴 글 목록 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "내가 공감한 게시글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = PostDefaultResponseDto.class))),
            @ApiResponse(
                    responseCode = "204",
                    description = "NO CONTENT"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/member/posts/like")
    public ResponseEntity<DefaultResponseDto<Object>> findLikedPostsByMember(
            HttpServletRequest request2
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        memberService.findById(loginMemberId);

        List<Long> foundPostIds = postLikedMemberService.findLikedPostIdsByMemberId(loginMemberId);

        if(foundPostIds.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("공감한 게시글이 없습니다.")
                            .build());
        }

        List<Post> resultPosts = new ArrayList<>();

        for(Long postId : foundPostIds) {
            Post post = postService.findById(postId);
            resultPosts.add(post);
        }

        List<PostDefaultResponseDto> response = resultPosts.stream()
                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 로그인한 멤버가 공감한 게시글 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "게시글 공감하기")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_LIKED / CANNOT_LIKED_SELF",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PutMapping("/member/post/{post-id}/like")
    public ResponseEntity<DefaultResponseDto<Object>> savePostLikes(
            HttpServletRequest request2, @PathVariable("post-id") Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        postService.addPostLikes(member, post);

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("NO_CONTENT")
                        .responseMessage("Post 게시글 공감하기 완료")
                        .build()
                );
    }

    @ApiOperation(value = "게시글 공감 취소하기")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO CONTENT"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "LIKED_NOT_FOUND / POST_LIKED_MEMBER_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @DeleteMapping("/member/post/{post-id}/like")
    public ResponseEntity<DefaultResponseDto<Object>> deletePostLikes(
            HttpServletRequest request2, @PathVariable("post-id") Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        postService.deletePostLikes(member, post);

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("NO_CONTENT")
                        .responseMessage("Post 게시글 공감 삭제 완료")
                        .build()
                );
    }

    @ApiOperation(value = "게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER / UNAUTHORIZED_DELETE",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @DeleteMapping("/member/post/{post-id}")
    public ResponseEntity<DefaultResponseDto<Object>> deletePost(
            HttpServletRequest request2, @PathVariable("post-id") Long postId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        postService.deletePost(postId,member);

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("NO_CONTENT")
                        .responseMessage("Post 삭제 완료")
                        .build()
                );
    }

    /**
     * Comment
     */
    @ApiOperation(value = "내가 작성한 댓글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = CommentDefaultResponseDto.class))),
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping(value = "/member/comments")
    public ResponseEntity<DefaultResponseDto<Object>> findCommentsByMember(
            HttpServletRequest request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        List<Comment> foundComments = commentService.findCommentsByMember(loginMember);

        if(foundComments.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("조회할 댓글이 없습니다.")
                            .build()
                    );
        }

        List<CommentDefaultResponseDto> response = foundComments.stream()
                .map(comment -> new CommentDefaultResponseDto(comment)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Comment 로그인한 멤버가 작성한 모든 댓글 조회 완료")
                        .data(response)
                        .build()
                );
    }

    /**
     * TODO: Comment 저장
     */
    @PostMapping(value = "/member/post/{post-id}/comment")
    @ApiOperation(value = "댓글/대댓글 저장")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = CommentDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<DefaultResponseDto<Object>> saveComment(
            HttpServletRequest request,
            @PathVariable("post-id") Long postId,
            @RequestBody @Valid CommentDefaultRequestDto requestDto
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        Comment createdComment = commentService.saveComment(
                requestDto.getContent(),
                requestDto.getIsAnonymous(),
                post,
                loginMember);

        // 대댓글이라면 댓글에 부모 댓글 식별자를 저장합니다.
        if(requestDto.getParentCommentId() != null) {
            commentService.saveParentCommentId(requestDto.getParentCommentId(), createdComment.getId());
        }

        CommentDefaultResponseDto response = new CommentDefaultResponseDto(createdComment);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Comment 저장 완료")
                        .data(response)
                        .build()
                );
    }

    /**
     * TODO: comment 삭제
     */
    @DeleteMapping(value = "/member/post/{post-id}/comment/{comment-id}")
    @ApiOperation(value = "댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = CommentDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
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
                        .responseCode("OK")
                        .responseMessage("Comment 삭제 완료")
                        .build()
                );
    }


    private void validateDormCategory(String dormNum) {
        if(!dormNum.equals("DORM1") && !dormNum.equals("DORM2") && !dormNum.equals("DORM3")) {
            throw new CustomException(DORM_CATEGORY_FORMAT_INVALID);
        }
    }

    private void validateDeletedPost(Post post) {
        if(post.getIsDeleted() == true) {
            throw new CustomException(DELETED_POST);
        }
    }
}
