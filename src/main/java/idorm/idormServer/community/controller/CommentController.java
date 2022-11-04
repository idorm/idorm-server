package idorm.idormServer.community.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
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

@RequiredArgsConstructor
@RestController
@Api(tags = "Community - Comment API")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/member/post/{postId}/comment")
    @ApiOperation(value = "Comment 저장")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment 저장 완료"),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
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
     * Comment 단건 조회 (해당 댓글의 전체 대댓글까지 조회되게)
     */
    @GetMapping(value = "/member/post/{postId}/comment/{commentId}")
    @ApiOperation(value = "Comment 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment 단건 조회 완료"),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 500, message = "Comment save 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findOneComment(
            HttpServletRequest request,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        Comment foundComment = commentService.findByPostIdAndCommentId(postId, commentId);

        CommentDefaultResponseDto response = new CommentDefaultResponseDto(foundComment);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Comment 단건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    /**
     * 로그인한 멤버가 작성한 모든 댓글들 조회
     */

    /**
     * Comment 삭제 (여전히 대댓글은 살아있어야함)
     */
}
