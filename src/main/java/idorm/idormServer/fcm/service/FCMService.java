package idorm.idormServer.fcm.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.*;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.fcm.dto.FcmRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;

import static idorm.idormServer.exception.ExceptionCode.FIREBASE_SERER_ERROR;
import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;
import static org.springframework.http.HttpHeaders.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FCMService {

    @Value("${firebase.project-id}")
    private String projectId;

    @Value("${firebase.private-key-id}")
    private String privateKeyId;

    @Value("${firebase.private-key}")
    private String privateKey;

    @Value("${firebase.client-email}")
    private String clientEmail;

    @Value("${firebase.client-id}")
    private String clientId;

    @Value("${firebase.client-url}")
    private String clientUrl;

    private final ObjectMapper objectMapper;


    public void sendMessage(FcmRequestDto fcmRequestDto) {

        log.info("========== [Before sendMessage] =========");
        log.info("token: " + fcmRequestDto.getToken() +
                "\n notifyType: " + fcmRequestDto.getNotification().getNotifyType() +
                "\n contentId: " + fcmRequestDto.getNotification().getContentId() +
                "\n title: " + fcmRequestDto.getNotification().getTitle() +
                "\n content: " + fcmRequestDto.getNotification().getContent());

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
                            .setNotification(AndroidNotification.builder()
                                    .setClickAction("FCM_PLUGIN_ACTIVITY") // background에서 onNotification 실행
                                    .build())
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

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("=============== Send Message Firebase Response ============");
            log.info(response);

//            String API_URL = "https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send";
//            String message = createMessage(fcmRequestDto);
//
//            OkHttpClient client = new OkHttpClient();
//            RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
//            Request request = new Request.Builder()
//                    .url(API_URL)
//                    .post(requestBody)
//                    .addHeader(AUTHORIZATION, "Bearer " + getAccessToken())
//                    .addHeader(CONTENT_TYPE, "application/json; UTF-8")
//                    .build();
//
//            log.info("========== [Processing sendMessage] =========");
//            log.info(request.body().toString());
//            log.info(request.toString());
//
//            Response response = client.newCall(request).execute();
//
//            log.info("========== [Finish sendMessage] =========");
//            log.info(response.body().toString());
//            log.info(response.toString());

        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        } catch (FirebaseMessagingException e) {
            throw new CustomException(e, FIREBASE_SERER_ERROR);
        }
    }

    private String createMessage(FcmRequestDto requestDto) {
        try {

            Message message = Message.builder()
                    .setToken(requestDto.getToken())
                    .setNotification(Notification.builder()
                            .setTitle(requestDto.getNotification().getTitle())
                            .setBody(requestDto.getNotification().getContent())
                            .build()
                    )
                    .setAndroidConfig(AndroidConfig.builder()
                            .setTtl(3600*1000) // 1hr
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .setNotification(AndroidNotification.builder()
                                    .setClickAction("FCM_PLUGIN_ACTIVITY") // background에서 onNotification 실행
                                    .build())
                            .build()
                    )
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setCategory("push_click")
                                    .setSound("default")
                                    .build())
                                .build()
                    )
                    .putData("channelId", requestDto.getNotification().getNotifyType().toString())
                    .putData("contentId", requestDto.getNotification().getContentId().toString())
                    .putData("title", requestDto.getNotification().getTitle())
                    .putData("content", requestDto.getNotification().getContent())
                    .build();

            return objectMapper.writeValueAsString(message);
        } catch (RuntimeException | IOException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    private String getAccessToken() {

        try {
            String jsonText = "{\"type\" : \"service_account\",\n" +
                    "\"project_id\" : \"" + projectId + "\",\n" +
                    "\"private_key_id\" : \"" + privateKeyId + "\",\n" +
                    "\"private_key\" : \"" + privateKey + "\",\n" +
                    "\"client_email\" : \"" + clientEmail + "\",\n" +
                    "\"client_id\" : \"" + clientId + "\",\n" +
                    "\"auth_uri\" : \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "\"token_uri\" : \"https://oauth2.googleapis.com/token\",\n" +
                    "\"auth_provider_x509_cert_url\" : \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "\"client_x509_cert_url\" : \"" + clientUrl + "\"\n" +
                    "}";

            InputStream is = new ByteArrayInputStream(jsonText.getBytes());

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(is)
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            // accessToken 생성
            googleCredentials.refreshIfExpired();

            // GoogleCredential의 getAccessToken()으로 토큰을 받아온 뒤, getTokenValue()로 최종적으로 받음
            // REST API로 FCM에 push 요청 보낼 때 Header에 설정하여 인증을 위해 사용
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (RuntimeException | IOException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
}
