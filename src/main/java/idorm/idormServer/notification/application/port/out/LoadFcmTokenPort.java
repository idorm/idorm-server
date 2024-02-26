package idorm.idormServer.notification.application.port.out;

import java.util.Optional;

import idorm.idormServer.notification.entity.FcmToken;

public interface LoadFcmTokenPort {

	Optional<FcmToken> load(Long memberId);
}
