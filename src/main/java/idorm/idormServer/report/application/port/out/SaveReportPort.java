package idorm.idormServer.report.application.port.out;

import idorm.idormServer.report.entity.Report;

public interface SaveReportPort {

	void save(Report report);
}
