package idorm.idormServer.notification.adapter.out.api;

import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.SendResponse;

import idorm.idormServer.notification.adapter.out.exception.FirebaseServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmNotificationClient implements NotificationClient {

	private final ObjectMapper objectMapper;
	private final FirebaseMessaging fcm;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async
	@Override
	public void notify(List<NotificationRequest> requests) {
		List<Message> messages = createMessages(requests);
		try {
			BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
			checkAllSuccess(response);
			log.info(response.getSuccessCount() + " messages were sent successfully");
		} catch (FirebaseMessagingException e) {
			log.warn("fail send FCM message", e);
			throw new FirebaseServerErrorException();
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async
	@Override
	public void notify(NotificationRequest request) {
		Message message = Message.builder()
			.setToken(request.getToken())
			.putAllData(objectMapper.convertValue(request.getMessage(), Map.class))
			.build();

		try {
			fcm.send(message);
		} catch (FirebaseMessagingException e) {
			log.warn("fail send FCM message", e);
			throw new FirebaseServerErrorException();
		}
	}

	private List<Message> createMessages(List<NotificationRequest> requests) {
		List<Message> messages = requests.stream()
			.map(request -> {
				Message message = Message.builder()
					.setToken(request.getToken())
					.putAllData(objectMapper.convertValue(request.getMessage(), Map.class))
					.build();
				return message;
			})
			.toList();
		return messages;
	}

	private void checkAllSuccess(BatchResponse response) {
		if (response.getFailureCount() > 0) {
			List<SendResponse> failSend = response.getResponses().stream()
				.filter(sendResponse -> !sendResponse.isSuccessful())
				.toList();
			log.warn("fcm 요청들에 대해 실패했습니다. {}", failSend);
		}
	}
}