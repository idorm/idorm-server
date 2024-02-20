package idorm.idormServer.notification.adapter.in.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import idorm.idormServer.notification.application.port.in.FcmTokenUseCase;
import idorm.idormServer.notification.application.port.in.dto.RegisterTokenRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegisterTokenEventListener {

	private final FcmTokenUseCase fcmTokenUseCase;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async
	@Transactional
	public void registerFcmToken(final RegisterTokenRequest request) {
		fcmTokenUseCase.save(request);
	}
}
