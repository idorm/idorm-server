package idorm.idormServer.notification.adapter.out.api;

import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import idorm.idormServer.notification.adapter.out.exception.FirebaseServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Profile("prod")
@Component
@RequiredArgsConstructor
public class FcmNotificationClient implements NotificationClient {

	private final ObjectMapper objectMapper;
	private final FirebaseMessaging fcm;

	@Override
	public void notify(List<NotificationRequest> requests) {
		List<Message> messages = createMessages(requests);
		try {
			BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
			log.info(response.getSuccessCount() + " messages were sent successfully");
		} catch (FirebaseMessagingException e) {
			throw new FirebaseServerErrorException();
		}
	}

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
			//TODO: "The registration token is not a valid FCM registration token" msg handling
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
}