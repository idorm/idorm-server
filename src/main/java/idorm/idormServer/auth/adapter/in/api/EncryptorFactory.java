package idorm.idormServer.auth.adapter.in.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import idorm.idormServer.auth.application.port.out.EncryptPort;

@Configuration
public class EncryptorFactory {

    @Bean
    public EncryptPort getEncryptor() {
        return new EncryptAdaptor();
    }
}

