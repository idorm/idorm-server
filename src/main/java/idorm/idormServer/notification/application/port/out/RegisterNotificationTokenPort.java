package idorm.idormServer.notification.application.port.out;

import idorm.idormServer.notification.application.port.in.dto.RegisterTokenRequest;

public interface RegisterNotificationTokenPort {

	void save(RegisterTokenRequest request);
}
