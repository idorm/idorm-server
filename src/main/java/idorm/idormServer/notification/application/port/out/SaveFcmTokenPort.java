package idorm.idormServer.notification.application.port.out;

import idorm.idormServer.notification.entity.FcmToken;

public interface SaveFcmTokenPort {

	void save(FcmToken fcmToken);
}
