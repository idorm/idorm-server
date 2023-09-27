package idorm.idormServer.community.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostLikedMember;
import idorm.idormServer.community.dto.*;
import idorm.idormServer.community.service.CommentService;
import idorm.idormServer.community.service.CommunityServiceFacade;
import idorm.idormServer.community.service.PostLikedMemberService;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.fcm.dto.FcmRequest;
import idorm.idormServer.fcm.dto.NotifyType;
import idorm.idormServer.fcm.service.FCMService;
import idorm.idormServer.matching.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import idorm.idormServer.photo.domain.PostPhoto;
import idorm.idormServer.photo.service.MemberPhotoService;
import idorm.idormServer.photo.service.PostPhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static idorm.idormServer.config.SecurityConfiguration.API_ROOT_URL_V1;
import static idorm.idormServer.config.SecurityConfiguration.AUTHENTICATION_HEADER_NAME;
import static idorm.idormServer.exception.ExceptionCode.*;

@Tag(name = "5. Community", description = "커뮤니티 api")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_ROOT_URL_V1 + "/member")
public class CommunityController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final PostService postService;
    private final PostLikedMemberService postLikedMemberService;
    private final CommentService commentService;
    private final PostPhotoService postPhotoService;
    private final FCMService fcmService;
    private final CommunityServiceFacade communityServiceFacade;
    private final MemberPhotoService memberPhotoService;

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
            HttpServletRequest servletRequest,
            @PathVariable(value = "dormitory-category") String dormCategoryRequest,
            @RequestParam(value = "page") int pageNum
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        memberService.findById(loginMemberId);
        
        DormCategory dormCategory = DormCategory.validateType(dormCategoryRequest);

        Page<Post> posts = postService.findManyPostsByDormCategory(dormCategory, pageNum);

        List<PostSummaryResponse> responses = posts.stream()
                .map(post -> new PostSummaryResponse(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_MANY_FOUND")
                        .responseMessage("Post 기숙사 필터링 후 게시글 다건 조회 완료")
                        .data(responses)
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
            HttpServletRequest servletRequest,
            @PathVariable("dormitory-category") String dormCategoryRequest
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        memberService.findById(loginMemberId);
        
        DormCategory dormCategory = DormCategory.validateType(dormCategoryRequest);

        List<Post> posts = postService.findTopPosts(dormCategory);

        List<PostSummaryResponse> responses = posts.stream()
                .map(post -> new PostSummaryResponse(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TOP_POST_MANY_FOUND")
                        .responseMessage("Post 인기 게시글 다건 조회 완료")
                        .data(responses)
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
            HttpServletRequest servletRequest,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);
        Post foundPost = postService.findById(postId);

        List<Comment> foundComments = commentService.findCommentsByPostId(postId);

        List<Member> anonymousMembers = new ArrayList<>();
        List<ParentCommentResponse> parentCommentResponses = new ArrayList<>();

        for(Comment comment : foundComments) {

            if(comment.getParentCommentId() == null) { // 대댓글이 아닌 댓글

                String parentAnonymousNickname = null;

                if (!comment.getMember().getIsDeleted() && comment.getIsAnonymous()) {
                    if (!anonymousMembers.contains(comment.getMember())) {
                        anonymousMembers.add(comment.getMember());
                    }
                    int index = anonymousMembers.indexOf(comment.getMember()) + 1;
                    parentAnonymousNickname = "익명" + index;
                }

                List<Comment> subComments =
                        commentService.findSubCommentsByParentCommentId(postId, comment.getId());

                if (subComments.isEmpty()) {
                    parentCommentResponses.add(new ParentCommentResponse(parentAnonymousNickname,
                            comment,
                            memberPhotoService.findByMember(comment.getMember()),
                            null));
                    continue;
                }

                List<CommentResponse> subCommentDtos = new ArrayList<>();
                for (Comment subComment : subComments) {

                    String subCommentAnonymousNickname = null;

                    if (!subComment.getMember().getIsDeleted() && subComment.getIsAnonymous()) {
                        if (!anonymousMembers.contains(subComment.getMember())) {
                            anonymousMembers.add(subComment.getMember());
                        }
                        int index = anonymousMembers.indexOf(subComment.getMember()) + 1;
                        subCommentAnonymousNickname = "익명" + index;
                    }
                    
                    subCommentDtos.add(new CommentResponse(subCommentAnonymousNickname,
                            subComment,
                            memberPhotoService.findByMember(subComment.getMember())));
                }
                parentCommentResponses.add(new ParentCommentResponse(parentAnonymousNickname,
                        comment,
                        memberPhotoService.findByMember(comment.getMember()),
                        subCommentDtos));
            }
        }
        boolean memberLikedPost = postLikedMemberService.isMemberLikedPost(member, foundPost);
        PostResponse response = new PostResponse(foundPost,
                parentCommentResponses,
                memberPhotoService.findByMember(foundPost.getMember()),
                memberLikedPost);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_FOUND")
                        .responseMessage("Post 단건 조회 완료")
                        .data(response)
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
            HttpServletRequest servletRequest,
            @ModelAttribute PostSaveRequest request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);

        postService.validatePostRequest(request.getTitle(), request.getContent(), request.getIsAnonymous());
        postService.validatePostPhotoCountExceeded(request.getFiles().size());

        Post post = communityServiceFacade.savePost(member, request);

        PostResponse response = new PostResponse(post, memberPhotoService.findByMember(member));

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_SAVED")
                        .responseMessage("Post 저장 완료")
                        .data(response)
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
            HttpServletRequest servletRequest,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId,
            @ModelAttribute PostUpdateRequest request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        postService.validatePostAuthorization(post, member);
        postService.validatePostRequest(request.getTitle(), request.getContent(), request.getIsAnonymous());

        List<PostPhoto> savedPostPhotos = postService.findAllPostPhotosFromPost(post);
        List<PostPhoto> deletePostPhotos = new ArrayList<>();

        for (Long deletePostPhotoId : request.getDeletePostPhotoIds()) {
            deletePostPhotos.add(postPhotoService.findById(post.getId(), deletePostPhotoId));
        }

        postService.validatePostPhotoCountExceeded(savedPostPhotos.size() - deletePostPhotos.size()
                + request.getFiles().size());

        communityServiceFacade.updatePost(post, request, deletePostPhotos);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_UPDATED")
                        .responseMessage("Post 수정 완료")
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
            HttpServletRequest servletRequest
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);
        List<Post> posts = postService.findPostsByMember(member);

        List<PostSummaryResponse> response = posts.stream()
                .map(post -> new PostSummaryResponse(post))
                .collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("POST_MANY_FOUND")
                        .responseMessage("Post 내가 쓴 글 목록 조회 완료")
                        .data(response)
                        .build()
                );
    }

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
            HttpServletRequest servletRequest
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        memberService.findById(loginMemberId);

        List<Long> likedPostIds = postLikedMemberService.findAllLikedPostIdByMemberId(loginMemberId);

        List<Post> likedPosts = new ArrayList<>();
        for(Long postId : likedPostIds) {
            postService.findOptionalById(postId)
                    .ifPresent(post -> likedPosts.add(post));
        }

        List<PostSummaryResponse> responses = likedPosts.stream()
                .map(post -> new PostSummaryResponse(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("LIKED_POST_MANY_FOUND")
                        .responseMessage("Post 로그인한 멤버가 공감한 게시글 조회 완료")
                        .data(responses)
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
            HttpServletRequest servletRequest,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        if (post.getMember().getIsDeleted())
            throw new CustomException(null, CANNOT_LIKED_POST_BY_DELETED_MEMBER);

        if(post.getMember().equals(member))
            throw new CustomException(null, CANNOT_LIKED_SELF);

        if (postLikedMemberService.isMemberLikedPost(member, post))
            throw new CustomException(null, DUPLICATE_LIKED);

        postLikedMemberService.create(member, post);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_LIKED_POST")
                        .responseMessage("Post 게시글 공감 완료")
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
            HttpServletRequest servletRequest,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        PostLikedMember postLikedMember = postLikedMemberService.findOneByPostAndMember(post, member);

        communityServiceFacade.deletePostLikes(post, postLikedMember);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_LIKED_POST_CANCELED")
                        .responseMessage("Post 게시글 공감 삭제 완료")
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
            HttpServletRequest servletRequest,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);

        Post post = postService.findById(postId);
        postService.validatePostAuthorization(post, member);

        List<PostLikedMember> postLikedMembersFromPost = postLikedMemberService.findAllByPost(post);
        List<PostPhoto> postPhotosFromPost = postPhotoService.findAllByPost(post);

        communityServiceFacade.deletePost(post, postLikedMembersFromPost, postPhotosFromPost);

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
    public ResponseEntity<DefaultResponseDto<Object>> saveComment (
            HttpServletRequest servletRequest,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId,
            @RequestBody @Valid CommentRequest request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);

        Post post = postService.findById(postId);
        if(request.getParentCommentId() != null)
            commentService.isExistCommentFromPost(postId, request.getParentCommentId());

        Comment comment = communityServiceFacade.saveComment(member, post, request);

        // 대댓글 달림, 게시글 주인 / 부모댓글 주인 / 대댓글 단 사람들한테 알람
        List<String> alertSubCommentMembersFcmTokens = new ArrayList<>();

        if (request.getParentCommentId() != null) { // 대댓글 알림

            // 게시글 주인
            if (post.getMember().getFcmToken() != null && !post.getMember().equals(member))
                alertSubCommentMembersFcmTokens.add(post.getMember().getFcmToken());

            // 부모 댓글 주인
            Optional<Comment> parentComment = commentService.findOptionalById(request.getParentCommentId());

            if (parentComment.isPresent() && parentComment.get().getMember().getFcmToken() != null
                    && !parentComment.get().getMember().equals(member)) {
                if (!alertSubCommentMembersFcmTokens.contains(parentComment.get().getMember().getFcmToken()))
                    alertSubCommentMembersFcmTokens.add(parentComment.get().getMember().getFcmToken());
            }

            // 대댓글 주인들에게 알람
            List<Comment> subComments = commentService.findSubCommentsByParentCommentId(post.getId(), request.getParentCommentId());

            for (Comment subComment : subComments) {

                if (!subComment.getIsDeleted() && !subComment.getMember().equals(member)
                        && subComment.getMember().getFcmToken() != null) {
                    if (!alertSubCommentMembersFcmTokens.contains(subComment.getMember().getFcmToken()))
                        alertSubCommentMembersFcmTokens.add(subComment.getMember().getFcmToken());
                }
            }

            for (String fcmToken : alertSubCommentMembersFcmTokens) {
                FcmRequest fcmRequestDto = FcmRequest.builder()
                        .token(fcmToken)
                        .notification(FcmRequest.Notification.builder()
                                .notifyType(NotifyType.SUBCOMMENT)
                                .contentId(postId)
                                .title("새로운 대댓글이 달렸어요: ")
                                .content(comment.getContent())
                                .build())
                        .build();
                fcmService.sendMessage(member, fcmRequestDto);
            }
            
        } else { // 댓글 알림

            // 게시글 주인에게 알람
            if (post.getMember().getFcmToken() != null && !post.getMember().equals(member)) {
                FcmRequest fcmRequestDto = FcmRequest.builder()
                        .token(post.getMember().getFcmToken())
                        .notification(FcmRequest.Notification.builder()
                                .notifyType(NotifyType.COMMENT)
                                .contentId(postId)
                                .title("새로운 댓글이 달렸어요: ")
                                .content(comment.getContent())
                                .build())
                        .build();
                fcmService.sendMessage(post.getMember(), fcmRequestDto);
            }
        }

        CommentResponse response = new CommentResponse(comment, memberPhotoService.findByMember(comment.getMember()));

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("COMMENT_SAVED")
                        .responseMessage("Comment 댓글 저장 완료")
                        .data(response)
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
            HttpServletRequest servletRequest,
            @PathVariable("post-id")
            @Positive(message = "게시글 식별자는 양수만 가능합니다.")
                Long postId,
            @PathVariable("comment-id")
            @Positive(message = "댓글 식별자는 양수만 가능합니다.")
                Long commentId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);

        Post post = postService.findById(postId);
        Comment comment = commentService.findByCommentIdAndPost(post, commentId);

        commentService.validateCommentAuthorization(comment, member);

        commentService.delete(comment);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("COMMENT_DELETED")
                        .responseMessage("Comment 삭제 완료")
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
            HttpServletRequest servletRequest
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);

        List<Comment> foundComments = commentService.findCommentsByMember(member);

        List<CommentResponse> response = foundComments.stream()
                .map(comment -> new CommentResponse(comment, memberPhotoService.findByMember(comment.getMember())))
                .collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("COMMENT_MANY_FOUND")
                        .responseMessage("Comment 로그인한 멤버가 작성한 모든 댓글 조회 완료")
                        .data(response)
                        .build()
                );
    }
}
