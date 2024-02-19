package idorm.idormServer.report.application.port.out;

import idorm.idormServer.report.domain.Report;

public interface SaveReportPort {

	void save(Report report);
}
