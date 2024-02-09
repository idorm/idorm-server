package idorm.idormServer.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

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

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        Optional<FirebaseApp> defaultFirebaseApp = defaultFirebaseApp();
        if (defaultFirebaseApp.isPresent()) {
            return FirebaseMessaging.getInstance(defaultFirebaseApp.get());
        }
        return FirebaseMessaging.getInstance(
                FirebaseApp.initializeApp(createFirebaseOption())
        );
    }

    private Optional<FirebaseApp> defaultFirebaseApp() {
        List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();
        if (firebaseAppList == null || firebaseAppList.isEmpty()) {
            return Optional.empty();
        }
        return firebaseAppList.stream()
                .filter(firebaseApp -> firebaseApp.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                .findAny();
    }

    private FirebaseOptions createFirebaseOption() throws IOException {
        return FirebaseOptions.builder()
                .setCredentials(createGoogleCredentials())
                .setProjectId(projectId)
                .build();
    }

    private GoogleCredentials createGoogleCredentials() throws IOException {
        return GoogleCredentials
                .fromStream(new ByteArrayInputStream(fcmPrivateKeyText()));
    }

    private byte[] fcmPrivateKeyText() {
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

        return jsonText.getBytes();
    }
}
