package idorm.idormServer.report.adapter.out.persistence;

import idorm.idormServer.member.adapter.out.persistence.MemberMapper;
import idorm.idormServer.report.domain.Report;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportMapper {

    private final MemberMapper memberMapper;

    public ReportJpaEntity toEntity(Report report) {
        return new ReportJpaEntity(report.getId(),
                memberMapper.toEntity(report.getReportedMember()),
                memberMapper.toEntity(report.getReporterMember()),
                report.getReportedPost(),
                report.getReportedComment(),
                report.getReasonType(),
                report.getReason(),
                report.getCreatedAt());
    }

    public Report toDomain(ReportJpaEntity reportJpaEntity) {
        return Report.forMapper(reportJpaEntity.getId(),
                memberMapper.toDomain(reportJpaEntity.getReportedMember()),
                memberMapper.toDomain(reportJpaEntity.getReporterMember()),
                reportJpaEntity.getReportedPost(),
                reportJpaEntity.getReportedComment(),
                reportJpaEntity.getReasonType(),
                reportJpaEntity.getReason(),
                reportJpaEntity.getCreatedAt());
    }
}