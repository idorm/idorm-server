package idorm.idormServer.auth.encryptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptorFactory {

    @Bean
    public EncryptorI getEncryptor() {
        return new Encryptor();
    }
}

