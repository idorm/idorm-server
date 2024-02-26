package idorm.idormServer.report.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.report.application.port.out.SaveReportPort;
import idorm.idormServer.report.entity.Report;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveReportAdapter implements SaveReportPort {

	private final ReportRepository reportRepository;

	@Override
	public void save(final Report report) {
		reportRepository.save(report);
	}
}