package idorm.idormServer.email.adapter.out.api;

import idorm.idormServer.email.application.port.out.SendEmailPort;
import idorm.idormServer.email.domain.Email;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!dev & !prod")
public class MockMailClient implements SendEmailPort {
    @Override
    public void send(Email email) {
        // no-op
    }
}
