package idorm.idormServer.notification.adapter.out.api;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
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
@Profile("prod")
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

/*
public List<Message> createMessage(Member member, List<FcmRequest> fcmRequestDtos) {
    if (member.getFcmToken() == null)
        return null;

    List<Message> messages = new ArrayList<>();

    for (FcmRequest fcmRequestDto : fcmRequestDtos) {

        Message message = Message.builder()
                .putData("channelId", fcmRequestDto.getNotification().getNotifyType().toString())
                .putData("contentId", fcmRequestDto.getNotification().getContentId().toString())
                .putData("title", fcmRequestDto.getNotification().getTitle())
                .putData("content", fcmRequestDto.getNotification().getContent())
                .setNotification(Notification.builder()
                        .setTitle(fcmRequestDto.getNotification().getTitle())
                        .setBody(fcmRequestDto.getNotification().getContent())
                        .build()
                )
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000) // 1hr
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                .setClickAction(fcmRequestDto.getNotification().getNotifyType().toString())
                                .setChannelId(fcmRequestDto.getNotification().getNotifyType().toString())
                                .build()
                        )
                        .build()
                )
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setSound("default")
                                .setContentAvailable(true)
                                .setCategory(fcmRequestDto.getNotification().getNotifyType().toString())
                                .build())
                        .build()
                )
                .setToken(member.getFcmToken())
                .build();

        messages.add(message);
    }
    return messages;
}

public List<Message> createMessageOfTeamCalendar(List<FcmRequest> fcmRequestDtos) {

    List<Message> messages = new ArrayList<>();

    for (FcmRequest fcmRequestDto : fcmRequestDtos) {

        Message message = Message.builder()
                .putData("channelId", fcmRequestDto.getNotification().getNotifyType().toString())
                .putData("contentId", fcmRequestDto.getNotification().getContentId().toString())
                .putData("title", fcmRequestDto.getNotification().getTitle())
                .putData("content", fcmRequestDto.getNotification().getContent())
                .setNotification(Notification.builder()
                        .setTitle(fcmRequestDto.getNotification().getTitle())
                        .setBody(fcmRequestDto.getNotification().getContent())
                        .build()
                )
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000) // 1hr
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                .setClickAction(fcmRequestDto.getNotification().getNotifyType().toString())
                                .setChannelId(fcmRequestDto.getNotification().getNotifyType().toString())
                                .build()
                        )
                        .build()
                )
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setSound("default")
                                .setContentAvailable(true)
                                .setCategory(fcmRequestDto.getNotification().getNotifyType().toString())
                                .build())
                        .build()
                )
                .setToken(fcmRequestDto.getToken())
                .build();

        messages.add(message);
    }
    return messages;
}

public void sendMessage(Member member, FcmRequest fcmRequestDto) {

    if (fcmRequestDto.getToken() == null)
        return;

    try {
        Message message = Message.builder()
                .putData("channelId", fcmRequestDto.getNotification().getNotifyType().toString())
                .putData("contentId", fcmRequestDto.getNotification().getContentId().toString())
                .putData("title", fcmRequestDto.getNotification().getTitle())
                .putData("content", fcmRequestDto.getNotification().getContent())
                .setNotification(Notification.builder()
                        .setTitle(fcmRequestDto.getNotification().getTitle())
                        .setBody(fcmRequestDto.getNotification().getContent())
                        .build()
                )
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000) // 1hr
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                .setClickAction(fcmRequestDto.getNotification().getNotifyType().toString())
                                .setChannelId(fcmRequestDto.getNotification().getNotifyType().toString())
                                .build()
                        )
                        .build()
                )
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setSound("default")
                                .setContentAvailable(true)
                                .setCategory(fcmRequestDto.getNotification().getNotifyType().toString())
                                .build())
                        .build()
                )
                .setToken(fcmRequestDto.getToken())
                .build();

        FirebaseMessaging.getInstance().send(message);

    } catch (IllegalArgumentException e) {
        memberService.deleteFcmToken(member);
        throw new CustomException(null, ILLEGAL_ARGUMENT_FCM_TOKEN);
    } catch (FirebaseMessagingException e) {

        if (e.getMessage().contains("account not found")) {
            memberService.deleteFcmToken(member);
            throw new CustomException(null, ILLEGAL_ARGUMENT_FCM_TOKEN);
        }

        throw new CustomException(e, FIREBASE_SERER_ERROR);
    }
}

public void sendManyMessages(List<Message> messages) {

    try {
        BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);

        log.info(response.getSuccessCount() + " messages were sent successfully");
    } catch (FirebaseMessagingException e) {

    }
}
 */