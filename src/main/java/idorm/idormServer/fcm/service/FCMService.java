package idorm.idormServer.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.fcm.domain.FcmChannel;
import idorm.idormServer.fcm.dto.FcmMessage;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;
import static org.springframework.http.HttpHeaders.*;

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


    public void sendMessage(FcmChannel channel, String targetToken, String title, String body) {

        try {
            String API_URL = "https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send";
            String message = createMessage(channel, targetToken, title, body);

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .addHeader(AUTHORIZATION, "Bearer " + getAccessToken())
                    .addHeader(CONTENT_TYPE, "application/json; UTF-8")
                    .build();

            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    public String createMessage(FcmChannel channel, String targetToken, String title, String body) {
        try {

            FcmMessage fcmMessage = FcmMessage.builder()
                    .message(FcmMessage.Message.builder()
                            .token(targetToken)
                            .notification(FcmMessage.Notification.builder()
                                    .alertType(channel.toString())
                                    .title(title)
                                    .body(body)
                                    .build())
                            .build())
                    .validateOnly(false)
                    .build();

            return objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new CustomException(SERVER_ERROR);
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

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }
}
