package idorm.idormServer.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import idorm.idormServer.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfiguration {

    @Value("${firebase.project-id}")
    private final String projectId;

    @PostConstruct
    public void firebaseInit(){
        String firebaseConfigPath = "serviceAccountKey.json";

        try {
            FileInputStream refreshToken = new FileInputStream(firebaseConfigPath);

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
