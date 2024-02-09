package idorm.idormServer.email.service;

import java.util.function.Consumer;
import org.springframework.mail.MailMessage;

public interface MailClient {
    void send(Consumer<MailMessage> mailMessageConsumer);
}
