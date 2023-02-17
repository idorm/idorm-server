package idorm.idormServer.community.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.dto.comment.CommentParentResponseDto;
import idorm.idormServer.community.dto.comment.CommentDefaultRequestDto;
import idorm.idormServer.community.dto.comment.CommentDefaultResponseDto;
import idorm.idormServer.community.dto.post.PostAbstractResponseDto;
import idorm.idormServer.community.dto.post.PostOneResponseDto;
import idorm.idormServer.community.service.CommentService;
import idorm.idormServer.community.service.PostLikedMemberService;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.community.dto.post.PostSaveRequestDto;
import idorm.idormServer.community.dto.post.PostUpdateRequestDto;

import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import idorm.idormServer.photo.domain.Photo;
import idorm.idormServer.photo.service.PhotoService;
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
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final PhotoService photoService;

    @ApiOperation(value = "기숙사별 홈화면 게시글 목록 조회", notes = "- 페이징 적용으로 page는 페이지 번호를 의미합니다.\n " +
            "- page는 0부터 시작하며 서버에서 10개 단위로 페이징해서 반환합니다.\n " +
            "- 서버에서 최신 순으로 정렬하여 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "POST_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = PostAbstractResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "DORMCATEGORY_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping("/posts/{dormitory-category}")
    public ResponseEntity<DefaultResponseDto<Object>> findPostsFilteredByCategory(
            @PathVariable(value = "dormitory-category") String dormCategoryRequest,
            @RequestParam(value = "page") int pageNum
    ) {
        DormCategory dormCategory = DormCategory.validateType(dormCategoryRequest);

        Page<Post> posts = postService.findManyPostsByDormCategory(dormCategory, pageNum);

        List<PostAbstractResponseDto> response = posts.stream()
                .map(post -> new PostAbstractResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_MANY_FOUND")
                        .responseMessage("Post 기숙사 필터링 후 게시글 다건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "기숙사별 인기 게시글 다건 조회", notes = "- 서버에서 공감 순으로 정렬 후 최신 순으로 정렬합니다.\n" +
            "- 인기 게시글은 10개 입니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "TOP_POST_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = PostAbstractResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "DORMCATEGORY_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping("/posts/{dormitory-category}/top")
    public ResponseEntity<DefaultResponseDto<Object>> findTopPostsFilteredByCategory(
            @PathVariable("dormitory-category") String dormCategoryRequest
    ) {
        DormCategory dormCategory = DormCategory.validateType(dormCategoryRequest);

        List<Post> posts = postService.findTopPosts(dormCategory);

        List<PostAbstractResponseDto> response = posts.stream()
                .map(post -> new PostAbstractResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TOP_POST_MANY_FOUND")
                        .responseMessage("Post 인기 게시글 다건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "게시글 단건 조회", notes = "- 댓글은 과거 순으로 정렬됩니다.\n")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "POST_FOUND",
                    content = @Content(schema = @Schema(implementation = PostOneResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "POSTID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping("/post/{post-id}")
    public ResponseEntity<DefaultResponseDto<Object>> findOnePost(
            HttpServletRequest request,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        
        Post foundPost = postService.findById(postId);

        List<Comment> foundComments = commentService.findCommentsByPostId(postId);

        List<Member> anonymousMembers = new ArrayList<>();
        List<CommentParentResponseDto> parentCommentResponses = new ArrayList<>();

        for(Comment comment : foundComments) {

            if(comment.getParentCommentId() == null) { // 대댓글이 아닌 댓글

                String parentAnonymousNickname = null;

                if (comment.getMember() != null && comment.getIsAnonymous() == true) {
                    if (!anonymousMembers.contains(comment.getMember())) {
                        anonymousMembers.add(comment.getMember());
                    }
                    int index = anonymousMembers.indexOf(comment.getMember()) + 1;
                    parentAnonymousNickname = "익명" + index;
                }

                List<Comment> subComments =
                        commentService.findSubCommentsByParentCommentId(postId, comment.getId());

                if (subComments.isEmpty()) {
                    parentCommentResponses.add(new CommentParentResponseDto(parentAnonymousNickname,
                            comment,
                            null));
                    continue;
                }

                List<CommentDefaultResponseDto> subCommentDtos = new ArrayList<>();
                for (Comment subComment : subComments) {

                    String subCommentAnonymousNickname = null;

                    if (subComment.getMember() != null && subComment.getIsAnonymous() == true) {
                        if (!anonymousMembers.contains(subComment.getMember())) {
                            anonymousMembers.add(subComment.getMember());
                        }
                        int index = anonymousMembers.indexOf(subComment.getMember()) + 1;
                        subCommentAnonymousNickname = "익명" + index;
                    }

                    CommentDefaultResponseDto subCommentDto = new CommentDefaultResponseDto(subCommentAnonymousNickname,
                            subComment);
                    subCommentDtos.add(subCommentDto);
                }
                parentCommentResponses.add(new CommentParentResponseDto(parentAnonymousNickname, comment, subCommentDtos));
            }
        }
        boolean memberLikedPost = postLikedMemberService.isMemberLikedPost(member, foundPost);
        PostOneResponseDto response = new PostOneResponseDto(foundPost, parentCommentResponses, memberLikedPost);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_FOUND")
                        .responseMessage("Post 단건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "게시글 저장", notes = "- 첨부 파일이 없다면 null 이 아닌 빈 배열로 보내주세요.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "POST_SAVED",
                    content = @Content(schema = @Schema(implementation = PostOneResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "DORMCATEGORY_CHARACTER_INVALID / FIELD_REQUIRED / TITLE_LENGTH_INVALID / " +
                            "CONTENT_LENGTH_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "413",
                    description = "FILE_SIZE_EXCEED / FILE_COUNT_EXCEED"),
            @ApiResponse(responseCode = "415",
                    description = "FILE_TYPE_UNSUPPORTED"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/post",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultResponseDto<Object>> savePost(
            HttpServletRequest request2,
            @ModelAttribute PostSaveRequestDto request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        postService.validatePostRequest(request.getTitle(), request.getContent(), request.getIsAnonymous());
        postService.validatePostPhotoCountExceeded(request.getFiles().size());

        Post post = postService.save(member, request.toEntity(member));

        photoService.savePostPhotos(post, request.getFiles());

        PostOneResponseDto response = new PostOneResponseDto(post);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_SAVED")
                        .responseMessage("Post 저장 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "게시글 수정", notes = "- 첨부 파일이 없다면 null 이 아닌 빈 배열로 보내주세요.\n" +
            "- 삭제할 게시글 사진(deletePostPhotoIds)이 없다면 404(POST_PHOTO_NOT_FOUND)를 보냅니다. ")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "POST_UPDATED",
                    content = @Content(schema = @Schema(implementation = PostOneResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / TITLE_LENGTH_INVALID / CONTENT_LENGTH_INVALID /" +
                            "POSTID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER / UNAUTHORIZED_POST"),
            @ApiResponse(responseCode = "404",
                    description = "DELETED_POST / POST_NOT_FOUND / POST_PHOTO_NOT_FOUND"),
            @ApiResponse(responseCode = "413",
                    description = "FILE_SIZE_EXCEED / FILE_COUNT_EXCEED"),
            @ApiResponse(responseCode = "415",
                    description = "FILE_TYPE_UNSUPPORTED"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping(value = "/post/{post-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultResponseDto<Object>> updatePost(
            HttpServletRequest request2,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId,
            @ModelAttribute PostUpdateRequestDto request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        postService.validatePostAuthorization(post, member);
        postService.validatePostRequest(request.getTitle(), request.getContent(), request.getIsAnonymous());

        List<Photo> savedPostPhotos = postService.findPostPhotosIsDeletedFalse(post);
        List<Photo> deletePostPhotos = new ArrayList<>();

        for (Long deletePostPhotoId : request.getDeletePostPhotoIds()) {
            deletePostPhotos.add(photoService.findById(post.getId(), deletePostPhotoId));
        }

        postService.validatePostPhotoCountExceeded(savedPostPhotos.size() - deletePostPhotos.size()
                + request.getFiles().size());

        postService.updatePost(post,
                request.getTitle(),
                request.getContent(),
                request.getIsAnonymous(),
                deletePostPhotos);

        photoService.savePostPhotos(post, request.getFiles());

        PostOneResponseDto response = new PostOneResponseDto(post);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_UPDATED")
                        .responseMessage("Post 수정 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "내가 쓴 글 목록 조회", notes = "- 서버에서 최신 순으로 정렬하여 응답힙니다.\n " +
            "- 이 API의 경우에는 게시글 생성일자가 아닌 수정일자로 정렬합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "POST_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = PostAbstractResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping("/posts/write")
    public ResponseEntity<DefaultResponseDto<Object>> findPostsByMember(
            HttpServletRequest request2
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        List<Post> posts = postService.findPostsByMember(member);

        List<PostAbstractResponseDto> response = posts.stream()
                .map(post -> new PostAbstractResponseDto(post))
                .collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_MANY_FOUND")
                        .responseMessage("Post 내가 쓴 글 목록 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "내가 공감한 게시글 목록 조회", notes = "- 서버에서 최신 순으로 정렬하여 응답힙니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "LIKED_POST_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = PostAbstractResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping("/posts/like")
    public ResponseEntity<DefaultResponseDto<Object>> findLikedPostsByMember(
            HttpServletRequest request2
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        memberService.findById(loginMemberId);

        List<Long> likedPostIds = postLikedMemberService.findAllLikedPostIdByMemberId(loginMemberId);

        List<Post> likedPosts = new ArrayList<>();
        for(Long postId : likedPostIds) {
            Post post = postService.findById(postId);
            likedPosts.add(post);
        }

        List<PostAbstractResponseDto> response = likedPosts.stream()
                .map(post -> new PostAbstractResponseDto(post)).collect(Collectors.toList());

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
            @ApiResponse(responseCode = "400",
                    description = "POSTID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST"),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_LIKED / CANNOT_LIKED_SELF"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PutMapping("/post/{post-id}/like")
    public ResponseEntity<DefaultResponseDto<Object>> savePostLikes(
            HttpServletRequest request2,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        postLikedMemberService.create(member, post);

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
            @ApiResponse(responseCode = "400",
                    description = "POSTID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST / POSTLIKEDMEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @DeleteMapping("/post/{post-id}/like")
    public ResponseEntity<DefaultResponseDto<Object>> deletePostLikes(
            HttpServletRequest request2,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        postLikedMemberService.delete(member, post);

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
            @ApiResponse(responseCode = "400",
                    description = "POSTID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER / UNAUTHORIZED_POST"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @DeleteMapping("/post/{post-id}")
    public ResponseEntity<DefaultResponseDto<Object>> deletePost(
            HttpServletRequest request2,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        Post post = postService.findById(postId);
        postService.validatePostAuthorization(post, member);
        postService.deletePost(post);

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
            notes = "- 대댓글인 경우, parentCommentId(부모 댓글 식별자)가 게시글에 존재하지 않는다면 404(COMMENT_NOT_FOUND)를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "COMMENT_SAVED",
                    content = @Content(schema = @Schema(implementation = CommentDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / POSTID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST / COMMENT_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/post/{post-id}/comment")
    public ResponseEntity<DefaultResponseDto<Object>> saveComment(
            HttpServletRequest request2,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId,
            @RequestBody @Valid CommentDefaultRequestDto request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        Post post = postService.findById(postId);
        if(request.getParentCommentId() != null)
            commentService.isExistCommentFromPost(postId, request.getParentCommentId());

        Comment comment = commentService.save(member, request.toEntity(member, post));

        if(request.getParentCommentId() != null)
            commentService.saveParentCommentId(request.getParentCommentId(), comment);

        CommentDefaultResponseDto response = new CommentDefaultResponseDto(comment);

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
                    description = "COMMENT_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "POSTID_NEGATIVEORZERO_INVALID / COMMENTID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER / UNAUTHORIZED_COMMENT"),
            @ApiResponse(responseCode = "404",
                    description = "POST_NOT_FOUND / DELETED_POST / COMMENT_NOT_FOUND / DELETED_COMMENT"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> deleteComment(
            HttpServletRequest request,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId,
            @PathVariable("comment-id")
            @Positive(message = "댓글 식별자는 양수만 가능합니다.")
                Long commentId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        Post post = postService.findById(postId);
        Comment comment = commentService.findByCommentIdAndPost(post, commentId);

        commentService.validateCommentAuthorization(comment, member);

        commentService.deleteComment(comment);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("COMMENT_DELETED")
                        .responseMessage("Comment 삭제 완료")
                        .build()
                );
    }

    @ApiOperation(value = "내가 작성한 댓글 목록 조회", notes = "- 서버에서 최신 순으로 정렬하여 응답합니다. \n" +
            "- 댓글은 수정 기능을 제공하지 않으므로 생성일자로 정렬합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "COMMENT_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = CommentDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping(value = "/comments")
    public ResponseEntity<DefaultResponseDto<Object>> findCommentsByMember(
            HttpServletRequest request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        List<Comment> foundComments = commentService.findCommentsByMember(member);

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
