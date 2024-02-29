package idorm.idormServer.notification.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.notification.application.port.out.DeleteFcmTokenPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteFcmTokenAdapter implements DeleteFcmTokenPort {

	private final FcmTokenRepository fcmTokenRepository;

	@Override
	public void deleteAll(final Long memberId) {
		fcmTokenRepository.deleteAllByMemberId(memberId);
	}
}
