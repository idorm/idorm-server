package idorm.idormServer.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import idorm.idormServer.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FirebaseConfiguration {

    @Value("${firebase.project-id}")
    private String projectId;

    @PostConstruct
    public void firebaseInit(){

        try {
            log.info("******");
            FileInputStream refreshToken = new FileInputStream("src/main/resources/serviceAccountKey.json");

            log.info("******" + refreshToken);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .setProjectId(projectId)
                    .build();

            log.info("******" + options);

            FirebaseApp.initializeApp(options);
        } catch (RuntimeException e){
            e.printStackTrace();
            throw new CustomException(SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }
}
