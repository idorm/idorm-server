package idorm.idormServer.auth.adapter.in.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import idorm.idormServer.notification.application.port.in.dto.RegisterTokenRequest;
import idorm.idormServer.notification.application.port.out.RegisterNotificationTokenPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationTokenRegisterClient implements RegisterNotificationTokenPort {

	private final ApplicationEventPublisher eventPublisher;

	@Override
	public void save(final RegisterTokenRequest request) {
		eventPublisher.publishEvent(request);
	}
}
