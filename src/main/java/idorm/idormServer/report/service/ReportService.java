package idorm.idormServer.report.service;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.report.domain.Report;
import idorm.idormServer.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;

    /**
     * 신고 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void save(Report report) {

        try {
            reportRepository.save(report);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 신고 확인 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(Report report) {
        try {
            report.delete();
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 회원 신고 검증 |
     * 409(MEMBER_CANNOT_SELFREPORT)
     */
    public void validateReportMember(Member reporterMember, Member reportedMember) {
        if (reportedMember.equals(reporterMember))
            throw new CustomException(MEMBER_CANNOT_SELFREPORT);
    }

    /**
     * 게시글 신고 검증 |
     * 409(POST_CANNOT_SELFREPORT)
     */
    public void validateReportPost(Member reporterMember, Post reportedPost) {
        if (reportedPost.getMember().equals(reporterMember))
            throw new CustomException(POST_CANNOT_SELFREPORT);
    }

    /**
     * 댓글 신고 검증 |
     * 409(COMMENT_CANNOT_SELFREPORT)
     */
    public void validateReportComment(Member reporterMember, Comment reportedComment) {
        if (reportedComment.getMember().equals(reporterMember))
            throw new CustomException(COMMENT_CANNOT_SELFREPORT);
    }
}
