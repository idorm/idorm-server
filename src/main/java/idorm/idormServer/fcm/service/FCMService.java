package idorm.idormServer.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.auth.oauth2.GoogleCredentials;
import idorm.idormServer.fcm.dto.FcmMessage;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FCMService {

    @Value("${firebase.project-id}")
    private String projectId;

    @Value("${FCM_PRIVATE_KEY_ID}")
    private String privateKeyId;

    @Value("${FCM_PRIVATE_KEY}")
    private String privateKey;

    @Value("${FCM_CLIENT_EMAIL}")
    private String clientEmail;

    @Value("${FCM_CLIENT_ID}")
    private String clientId;

    @Value("${FCM_CLIENT_URL}")
    private String clientUrl;

    private final ObjectMapper objectMapper;

    class FcmKey {

        public String type;
        public String project_id;
        public String private_key_id;
        public String private_key;
        public String client_email;
        public String client_id;
        public String auth_uri;
        public String token_uri;
        public String auth_provider_x509_cert_url;
        public String client_x509_cert_url;

        public FcmKey(String type,
                      String auth_uri,
                      String token_uri,
                      String auth_provider_x509_cert_url) {
            this.type = type;
            this.project_id = projectId;
            this.private_key_id = privateKeyId;
            this.private_key = privateKey;
            this.client_email = clientEmail;
            this.client_id = clientId;
            this.auth_uri = auth_uri;
            this.token_uri = token_uri;
            this.auth_provider_x509_cert_url = auth_provider_x509_cert_url;
            this.client_x509_cert_url = clientUrl;
        }
    }

    public void sendMessage(String targetToken, String title, String body) throws IOException {

        String API_URL = "https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send";
        String message = createMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }

    public String createMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build())
                        .build())
                .validateOnly(false)
                .build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {

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

        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Map<String, Object> map = new HashMap<>();
        map = objectMapper.readValue(jsonText, new TypeReference<Map<String, Object>>(){});

        String path = "src/main/resources/serviceAccountKey.json";
        objectMapper.writeValue(new File(path), map);

        FileInputStream refreshToken = new FileInputStream("src/main/resources/serviceAccountKey.json");

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(path).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
