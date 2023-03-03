package idorm.idormServer.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import idorm.idormServer.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@Configuration
public class FirebaseConfiguration {

    @Value("${firebase.project-id}")
    private String projectId;

    @PostConstruct
    public void firebaseInit(){

        try {
            FileInputStream refreshToken =
                    new FileInputStream("src/main/resources/serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .setProjectId(projectId)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e){
            throw new CustomException(SERVER_ERROR);
        }
    }
}
