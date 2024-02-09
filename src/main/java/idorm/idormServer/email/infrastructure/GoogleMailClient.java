package idorm.idormServer.email.infrastructure;

import idorm.idormServer.email.service.MailClient;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class GoogleMailClient implements MailClient {

    private final MailSender mailSender;
    private final String adminMail;

    public GoogleMailClient(MailSender mailSender, @Value("${spring.mail.username}") String adminMail) {
        this.mailSender = mailSender;
        this.adminMail = adminMail;
    }

    @Override
    @Async
    public void send(Consumer<MailMessage> mailMessageConsumer) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessageConsumer.accept(mailMessage);
        mailMessage.setFrom(adminMail);
        mailSender.send(mailMessage);
    }
}
