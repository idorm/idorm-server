package idorm.idormServer.notification.adapter.out.persistence;

import java.time.LocalDateTime;

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

	@Override
	public void deleteInactiveUserTokens() {
		fcmTokenRepository.deleteAllByUpdatedAtBefore(LocalDateTime.now().minusMonths(2));
	}
}