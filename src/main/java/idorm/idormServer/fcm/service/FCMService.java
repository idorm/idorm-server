package idorm.idormServer.fcm.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.messaging.*;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.fcm.dto.FcmRequestDto;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static idorm.idormServer.exception.ExceptionCode.FIREBASE_SERER_ERROR;
import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FCMService {

    private final MemberService memberService;

    public void sendMessage(Member member, FcmRequestDto fcmRequestDto) {

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
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        } catch (FirebaseMessagingException e) {

            if (e.getHttpResponse().getStatusCode() == 404)
                memberService.deleteFcmToken(member);
            else
                throw new CustomException(e, FIREBASE_SERER_ERROR);
        }
    }
}
