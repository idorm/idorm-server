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

import static idorm.idormServer.exception.ExceptionCode.ILLEGAL_ARGUMENT_SELF;
import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

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
            throw new CustomException(e, SERVER_ERROR);
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
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원 신고 검증 |
     * 400(ILLEGAL_ARGUMENT_SELF)
     */
    public void validateReportMember(Member reporterMember, Member reportedMember) {
        if (reportedMember.equals(reporterMember))
            throw new CustomException(null, ILLEGAL_ARGUMENT_SELF);
    }

    /**
     * 게시글 신고 검증 |
     * 400(ILLEGAL_ARGUMENT_SELF)
     */
    public void validateReportPost(Member reporterMember, Post reportedPost) {
        if (reportedPost.getMember().equals(reporterMember))
            throw new CustomException(null, ILLEGAL_ARGUMENT_SELF);
    }

    /**
     * 댓글 신고 검증 |
     * 400(ILLEGAL_ARGUMENT_SELF)
     */
    public void validateReportComment(Member reporterMember, Comment reportedComment) {
        if (reportedComment.getMember().equals(reporterMember))
            throw new CustomException(null, ILLEGAL_ARGUMENT_SELF);
    }
}