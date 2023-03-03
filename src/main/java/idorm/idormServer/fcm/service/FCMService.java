package idorm.idormServer.community.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.*;
import idorm.idormServer.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FCMService {

    @Value("${firebase.project-id}")
    private String projectId;

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send";
    private final ObjectMapper objectMapper;

    public String sendMessage(int requestId, String registrationToken) {

        try {
            Message message = Message.builder()
                    .setAndroidConfig(AndroidConfig.builder()
                            .setTtl(3600*1000)
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .setRestrictedPackageName("org.appcenter.inudorm")
                            .setDirectBootOk(true)
                            .setNotification(AndroidNotification.builder()
                                    .setTitle("[idorm] 오늘의 인기 게시글!") // TODO: 수정
                                    .setBody("오늘의 인기게시글 본문입니다.") // TODO: 수정
                                    .build())
                            .build())
                    .putData("requestId", String.valueOf(requestId))
                    .setToken(registrationToken)
                    .build();

            return FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            throw new CustomException(SERVER_ERROR);
        }
    }
}
