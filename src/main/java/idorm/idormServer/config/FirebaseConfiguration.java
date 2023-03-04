package idorm.idormServer.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import idorm.idormServer.exception.CustomException;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfiguration {

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
