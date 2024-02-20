package idorm.idormServer.notification.application.port.out;

import java.util.Optional;

import idorm.idormServer.notification.domain.FcmToken;

public interface LoadFcmTokenPort {

	Optional<FcmToken> load(Long memberId);
}
