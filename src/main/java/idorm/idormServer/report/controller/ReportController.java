package idorm.idormServer.report.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.service.CommentService;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import idorm.idormServer.report.domain.ReportType;
import idorm.idormServer.report.dto.ReportRequest;
import idorm.idormServer.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static idorm.idormServer.config.SecurityConfiguration.API_ROOT_URL_V1;
import static idorm.idormServer.config.SecurityConfiguration.AUTHENTICATION_HEADER_NAME;

@Tag(name = "X. Report", description = "신고 api")
@Validated
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping(API_ROOT_URL_V1 + "/member/report")
public class ReportController {

    private final ReportService reportService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    @Operation(summary = "회원/게시글/댓글/대댓글 신고")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "*_REPORTED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / MEMBERORPOSTORCOMMENTID_NEGATIVEORZERO_INVALID / " +
                            "*_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_PASSWORD"),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND / POST_NOT_FOUND / COMMENT_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> reportMemberOrPostOrComment(
            HttpServletRequest servletRequest,
            @RequestBody @Valid ReportRequest request) {

        long loginMemberId =
                Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);

        ReportType reportType = ReportType.validateType(request.getReportType());
        if (reportType.equals(ReportType.MEMBER)) {

            Member reportedMember = memberService.findById(request.getMemberOrPostOrCommentId());

            reportService.validateReportMember(member, reportedMember);
            reportService.save(request.toMemberReportEntity(member, reportedMember));

            return ResponseEntity.status(201)
                    .body(DefaultResponseDto.builder()
                            .responseCode("MEMBER_REPORTED")
                            .responseMessage("회원 신고 완료")
                            .build());
        } else if (reportType.equals(ReportType.POST)){

            Post reportedPost = postService.findById(request.getMemberOrPostOrCommentId());

            reportService.validateReportPost(member, reportedPost);
            reportService.save(request.toPostReportEntity(member, reportedPost));

            return ResponseEntity.status(201)
                    .body(DefaultResponseDto.builder()
                            .responseCode("POST_REPORTED")
                            .responseMessage("커뮤니티 게시글 신고 완료")
                            .build());
        } else {

            Comment reportedComment = commentService.findById(request.getMemberOrPostOrCommentId());

            reportService.validateReportComment(member, reportedComment);
            reportService.save(request.toCommentReportEntity(member, reportedComment));

            return ResponseEntity.status(201)
                    .body(DefaultResponseDto.builder()
                            .responseCode("COMMENT_REPORTED")
                            .responseMessage("커뮤니티 댓글 신고 완료")
                            .build());
        }
    }
}