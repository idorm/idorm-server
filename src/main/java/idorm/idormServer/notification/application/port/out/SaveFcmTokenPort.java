package idorm.idormServer.notification.application.port.out;

import idorm.idormServer.notification.domain.FcmToken;

public interface SaveFcmTokenPort {

	void save(FcmToken fcmToken);
}
