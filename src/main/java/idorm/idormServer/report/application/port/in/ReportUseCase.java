package idorm.idormServer.report.application.port.in;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.report.application.port.in.dto.ReportRequest;

public interface ReportUseCase {

	void reportMember(AuthResponse auth, ReportRequest request);

	void reportPost(AuthResponse auth, ReportRequest request);

	void reportComment(AuthResponse auth, ReportRequest request);
}
