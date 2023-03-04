package idorm.idormServer.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import idorm.idormServer.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FirebaseConfiguration {

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
            this.private_key = privateKey.replace("\n", "");
            this.client_email = clientEmail;
            this.client_id = clientId;
            this.auth_uri = auth_uri;
            this.token_uri = token_uri;
            this.auth_provider_x509_cert_url = auth_provider_x509_cert_url;
            this.client_x509_cert_url = clientUrl;
        }
    }

    @PostConstruct
    public void firebaseInit(){

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

            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            Map<String, Object> map = new HashMap<>();
            map = objectMapper.readValue(jsonText, new TypeReference<Map<String, Object>>(){});

            String path = "src/main/resources/serviceAccountKey.json";
            objectMapper.writeValue(new File(path), map);

            FileInputStream refreshToken = new FileInputStream("src/main/resources/serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .setProjectId(projectId)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e){
            e.printStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }
}
