package idorm.idormServer.fcm.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.messaging.*;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.fcm.dto.FcmRequestDto;
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

    public void sendMessage(FcmRequestDto fcmRequestDto) {

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
                            .setTtl(3600*1000) // 1hr
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .setNotification(null)
                            .build()
                    )
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setCategory("push_click")
                                    .setSound("default")
                                    .build())
                            .build()
                    )
                    .setToken(fcmRequestDto.getToken())
                    .build();
            FirebaseMessaging.getInstance().send(message);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        } catch (FirebaseMessagingException e) {
            throw new CustomException(e, FIREBASE_SERER_ERROR);
        }
    }
}
