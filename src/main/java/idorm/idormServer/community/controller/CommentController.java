package idorm.idormServer.community.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.dto.comment.CommentCustomResponseDto;
import idorm.idormServer.community.dto.comment.CommentDefaultRequestDto;
import idorm.idormServer.community.dto.comment.CommentDefaultResponseDto;
import idorm.idormServer.community.service.CommentService;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(tags = "Community - Comment API")
public class CommentController {

    // 게시글에서 댓글, 대댓글 전체 조회
    // 내가 쓴 전체 댓글 조회
    // 댓글 수정
    // 댓글, 대댓글 삭제

    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/member/post/{postId}/comment")
    @ApiOperation(value = "Comment 댓글 혹은 대댓글 저장")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment 저장 완료"),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 404, message = "게시글 혹은 부모 댓글을 조회할 수 없습니다."),
            @ApiResponse(code = 409, message = "부모 댓글과 대댓글 식별자는 같을 수 없습니다."),
            @ApiResponse(code = 500, message = "Comment save 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> saveComment(
            HttpServletRequest request,
            @PathVariable("postId") Long postId,
            @RequestBody @Valid CommentDefaultRequestDto requestDto
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
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
     * 해당 게시글에서 조회되는 모든 댓글 및 대댓글 조회
     */
    @GetMapping(value = "/member/post/{postId}/comments")
    @ApiOperation(value = "Comment 특정 게시글에서 조회되는 모든 댓글 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment 로그인한 멤버가 작성한 모든 댓글 조회 완료"),
            @ApiResponse(code = 204, message = "조회할 댓글이 없습니다."),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 404, message = "조회할 게시글이 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Comment 로그인한 멤버가 작성한 모든 댓글 조회 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findCommentsByPost(
            HttpServletRequest request,
            @PathVariable("postId") Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        List<Comment> foundComments = commentService.findCommentsByPostId(postId);

        if(foundComments.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("조회할 댓글이 없습니다.")
                            .build()
                    );
        }

        List<CommentCustomResponseDto> customResponseDtos = new ArrayList<>();
        for(Comment comment : foundComments) {
            // 부모 식별자를 가지고 있지 않다면, 해당 부모 식별자를 가지고 있는 댓글 리스트와 함께 dto 생성
            if(comment.getParentCommentId() == null) { // 대댓글이 아닌 댓글임

                // TODO: foundComments에서 가져와서 바꾸기
                List<Comment> foundSubComments = commentService.findSubCommentsByParentCommentId(postId, comment.getId());

                List<CommentDefaultResponseDto> defaultResponseDtos = new ArrayList<>();
                for (Comment subComment : foundSubComments) {
                    CommentDefaultResponseDto commentDefaultResponseDto = new CommentDefaultResponseDto(subComment);
                    defaultResponseDtos.add(commentDefaultResponseDto);
                }

                CommentCustomResponseDto customResponseDto = new CommentCustomResponseDto(comment, defaultResponseDtos);
                customResponseDtos.add(customResponseDto);
            }
        }

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Comment 특정 게시글에서 조회되는 모든 댓글 완료")
                        .data(customResponseDtos)
                        .build()
                );
    }

    /**
     * 로그인한 멤버가 작성한 모든 댓글 및 대댓글 조회
     */
    @GetMapping(value = "/member/comments/me")
    @ApiOperation(value = "Comment 로그인한 멤버가 작성한 모든 댓글 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment 로그인한 멤버가 작성한 모든 댓글 조회 완료"),
            @ApiResponse(code = 204, message = "조회할 댓글이 없습니다."),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 500, message = "Comment 로그인한 멤버가 작성한 모든 댓글 조회 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findCommentsByMember(
            HttpServletRequest request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
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
     * Comment 단건 댓글, 대댓글 삭제
     */
    @DeleteMapping(value = "/member/post/{postId}/comment/{commentId}")
    @ApiOperation(value = "Comment 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment 삭제 완료"),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 404, message = "조회할 게시글 혹은 댓글이 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "본인이 작성한 댓글만 삭제할 수 있습니다."),
            @ApiResponse(code = 500, message = "Comment 삭제 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> deleteComment(
            HttpServletRequest request,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
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
}
