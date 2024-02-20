package idorm.idormServer.notification.application.port.in;

import idorm.idormServer.notification.application.port.in.dto.RegisterTokenRequest;

public interface FcmTokenUseCase {

	void save(RegisterTokenRequest event);
}
