package idorm.idormServer.report.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_member_id")
    private Member reportedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_member_id")
    private Member reporterMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_post_id")
    private Post reportedPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_comment_id")
    private Comment reportedComment;

    private Character reasonType;
    private String reason;

    @Builder(builderClassName = "MemberReportBuilder", builderMethodName = "MemberReportBuilder")
    public Report(Member reporterMember, Member reportedMember, MemberReason reasonType, String reason) {
        this.reportedMember = reportedMember;
        this.reporterMember = reporterMember;
        this.reportedPost = null;
        this.reportedComment = null;
        this.reasonType = reasonType.getType();
        this.reason = reason;
        this.setIsDeleted(false);

        this.reportedMember.incrementreportedCount();
    }

    @Builder(builderClassName = "PostReportBuilder", builderMethodName = "PostReportBuilder")
    public Report(Member reporterMember,
                  Member reportedMember,
                  Post reportedPost,
                  CommunityReason reasonType,
                  String reason) {
        this.reporterMember = reporterMember;
        this.reportedMember = reportedMember;
        this.reportedPost = reportedPost;
        this.reportedComment = null;
        this.reasonType = reasonType.getType();
        this.reason = reason;
        this.setIsDeleted(false);

        this.reportedPost.incrementReportedCount();
    }

    @Builder(builderClassName = "CommentReportBuilder", builderMethodName = "CommentReportBuilder")
    public Report(Member reporterMember,
                  Member reportedMember,
                  Comment reportedComment,
                  CommunityReason reasonType,
                  String reason) {
        this.reportedMember = reportedMember;
        this.reporterMember = reporterMember;
        this.reportedComment = reportedComment;
        this.reasonType = reasonType.getType();
        this.reason = reason;
        this.setIsDeleted(false);

        this.reportedComment.incrementReportedCount();
    }

    public void delete() {
        this.setIsDeleted(true);
    }
}
