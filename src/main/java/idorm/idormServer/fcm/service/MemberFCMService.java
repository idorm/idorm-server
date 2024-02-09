package idorm.idormServer.fcm.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.messaging.*;
import idorm.idormServer.auth.dto.AuthInfo;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.fcm.dto.FcmRequest;
import idorm.idormServer.fcm.repository.MemberFCMRepository;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import idorm.idormServer.support.token.AuthorizationExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static idorm.idormServer.common.exception.ExceptionCode.FIREBASE_SERER_ERROR;
import static idorm.idormServer.common.exception.ExceptionCode.ILLEGAL_ARGUMENT_FCM_TOKEN;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFCMService {

    private final MemberFCMRepository memberFCMRepository;
    private final MemberService memberService;
    private final AuthorizationExtractor authorizationExtractor;

    @Async
    public void saveMemberFCM(AuthInfo authInfo, FcmRequest request) {

    }

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
}
