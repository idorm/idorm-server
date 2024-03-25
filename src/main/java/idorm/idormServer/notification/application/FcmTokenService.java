package idorm.idormServer.notification.application;

import static org.jsoup.internal.StringUtil.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.notification.application.port.in.FcmTokenUseCase;
import idorm.idormServer.notification.application.port.in.dto.RegisterTokenRequest;
import idorm.idormServer.notification.application.port.out.DeleteFcmTokenPort;
import idorm.idormServer.notification.application.port.out.LoadFcmTokenPort;
import idorm.idormServer.notification.application.port.out.SaveFcmTokenPort;
import idorm.idormServer.notification.entity.FcmToken;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FcmTokenService implements FcmTokenUseCase {

	private final LoadFcmTokenPort loadFcmTokenPort;
	private final SaveFcmTokenPort saveFcmTokenPort;
	private final DeleteFcmTokenPort deleteFcmTokenPort;

	@Override
	public void save(final RegisterTokenRequest event) {
		if (isBlank(event.fcmToken())) {
			deleteFcmTokenPort.deleteAll(event.memberId());
			return;
		}

		loadFcmTokenPort.load(event.memberId())
			.ifPresentOrElse(fcmToken -> fcmToken.updateToken(event.fcmToken()),
				() -> saveFcmTokenPort.save(new FcmToken(event.memberId(), event.fcmToken())));
	}

	@Override
	public void expire(final Long memberId) {
		deleteFcmTokenPort.deleteAll(memberId);
	}

	@Override
	public void expireOfExpiredMembers() {
		deleteFcmTokenPort.deleteInactiveUserTokens();
	}
}
