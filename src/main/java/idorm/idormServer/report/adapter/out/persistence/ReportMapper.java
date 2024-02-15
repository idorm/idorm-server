package idorm.idormServer.report.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.adapter.out.persistence.CommentMapper;
import idorm.idormServer.community.adapter.out.persistence.PostMapper;
import idorm.idormServer.member.adapter.out.persistence.MemberMapper;
import idorm.idormServer.report.domain.Report;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportMapper {

	private final MemberMapper memberMapper;
	private final PostMapper postMapper;
	private final CommentMapper commentMapper;

	public ReportJpaEntity toEntity(Report report) {
		return new ReportJpaEntity(report.getId(),
			memberMapper.toEntity(report.getReportedMember()),
			memberMapper.toEntity(report.getReporterMember()),
			postMapper.toEntity(report.getReportedPost()),
			commentMapper.toEntity(report.getReportedComment()),
			report.getReasonType(),
			report.getReason(),
			report.getCreatedAt());
	}

	public List<ReportJpaEntity> toEntity(List<Report> reports) {
		List<ReportJpaEntity> result = reports.stream()
			.map(this::toEntity)
			.toList();
		return result;
	}

	public Report toDomain(ReportJpaEntity reportJpaEntity) {
		return Report.forMapper(reportJpaEntity.getId(),
			memberMapper.toDomain(reportJpaEntity.getReportedMember()),
			memberMapper.toDomain(reportJpaEntity.getReporterMember()),
			postMapper.toDomain(reportJpaEntity.getReportedPost()),
			commentMapper.toDomain(reportJpaEntity.getReportedComment()),
			reportJpaEntity.getReasonType(),
			reportJpaEntity.getReason(),
			reportJpaEntity.getCreatedAt());
	}

	public List<Report> toDomain(List<ReportJpaEntity> entities) {
		List<Report> result = entities.stream()
			.map(this::toDomain)
			.toList();
		return result;
	}
}