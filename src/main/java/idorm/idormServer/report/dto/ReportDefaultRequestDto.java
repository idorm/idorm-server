package idorm.idormServer.report.dto;

import idorm.idormServer.common.ValidationSequence;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.report.domain.CommunityReason;
import idorm.idormServer.report.domain.MemberReason;
import idorm.idormServer.report.domain.Report;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@GroupSequence({ ReportDefaultRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Positive.class,
        ValidationSequence.Size.class})
@ApiModel(value = "Report 기본 요청")
public class ReportDefaultRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "신고 종류: MEMBER(회원), POST(게시글), COMMENT(댓글)",
            example = "MEMBER",
            allowableValues = "MEMBER, POST, COMMENT")
    @NotBlank(message = "모든 필수 항목을 입력해주세요.", groups = ValidationSequence.NotBlank.class)
    private String reportType;

    @ApiModelProperty(position = 2, required = true, value = "신고 회원 또는 신고 게시글 또는 신고 댓글 식별자", example = "1")
    @NotNull(message = "모든 필수 항목을 입력해주세요.", groups = ValidationSequence.NotNull.class)
    @Positive(message = "식별자는 양수만 가능합니다.", groups = ValidationSequence.Positive.class)
    private Long memberOrPostOrCommentId;

    @ApiModelProperty(position = 3, required = true,
            value = "사유 종류: 회원('NICKNAME', 'PROFILE_PHOTO', 'MATCHINGINFO', 'ETC'), " +
                    "커뮤니티('PLASTER', 'ADVERTISING_SPAM', 'OBSCENE_MATERIAL', 'ABUSE', 'FALSE_INFORMATION', 'ETC')",
            allowableValues = "NICKNAME(닉네임), PROFILE_PHOTO(프로필 사진), MATCHINGINFO(온보딩 정보), " +
                    "PLASTER(도배), ADVERTISING_SPAM(광고 / 스팸), OBSCENE_MATERIAL(음란물 / 선정성), " +
                    "ABUSE(욕설), FALSE_INFORMATION(사칭 / 허위 정보), ETC(기타)",
            example = "NICKNAME")
    @NotBlank(message = "모든 필수 항목을 입력해주세요.", groups = ValidationSequence.NotBlank.class)
    private String reasonType;

    @ApiModelProperty(position = 4, value = "사유", example = "사유")
    @Size(max = 200, message = "사유는 0~200자만 가능합니다.", groups = ValidationSequence.Size.class)
    private String reason;

    public Report toMemberReportEntity(Member reporterMember, Member reportedMember) {
        return Report.MemberReportBuilder()
                .reporterMember(reporterMember)
                .reportedMember(reportedMember)
                .reasonType(MemberReason.validateType(this.reasonType))
                .reason(this.reason)
                .build();
    }

    public Report toPostReportEntity(Member reporterMember, Post reportedPost) {
        return Report.PostReportBuilder()
                .reporterMember(reporterMember)
                .reportedMember(reportedPost.getMember())
                .reportedPost(reportedPost)
                .reasonType(CommunityReason.validateType(this.reasonType))
                .reason(this.reason)
                .build();
    }

    public Report toCommentReportEntity(Member reporterMember, Comment reportedComment) {
        return Report.CommentReportBuilder()
                .reporterMember(reporterMember)
                .reportedMember(reportedComment.getMember())
                .reportedComment(reportedComment)
                .reasonType(CommunityReason.validateType(this.reasonType))
                .reason(this.reason)
                .build();
    }
}
