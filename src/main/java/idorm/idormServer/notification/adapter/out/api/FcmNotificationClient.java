package idorm.idormServer.notification.adapter.out.api;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import idorm.idormServer.notification.adapter.out.exception.FirebaseServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmNotificationClient implements NotificationClient {

	private final ObjectMapper objectMapper;
	private final FirebaseMessaging messaging;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async("asyncExecutor")
	@Override
	public void notify(List<NotificationRequest> requests) {
		if (requests.isEmpty() || Objects.isNull(requests)) {
			return;
		}

		List<Message> messages = requests.stream()
			.map(request -> {
				Message message = Message.builder()
					.setToken(request.getToken())
					.putAllData(objectMapper.convertValue(request.getMessage(), Map.class))
					.build();
				return message;
			})
			.toList();
		try {
			BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
			log.info(response.getSuccessCount() + " messages were sent successfully");
		} catch (FirebaseMessagingException e) {
			log.warn("fail send FCM message", e);
			throw new FirebaseServerErrorException();
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async("asyncExecutor")
	@Override
	public void notify(NotificationRequest request) {
		if (request == null) {
			return;
		}

		Message message = Message.builder()
			.setToken(request.getToken())
			.putAllData(objectMapper.convertValue(request.getMessage(), Map.class))
			.build();
		try {
			messaging.send(message);
		} catch (FirebaseMessagingException e) {
			log.warn("fail send FCM message", e);
			throw new FirebaseServerErrorException();
		}
	}
}