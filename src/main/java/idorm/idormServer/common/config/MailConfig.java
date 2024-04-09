package idorm.idormServer.common.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Value("${spring.mail.host}")
	private String HOST;

	@Value("${spring.mail.port}")
	private int PORT;

	@Value("${spring.mail.username}")
	private String USERNAME;

	@Value("${spring.mail.password}")
	private String PASSWORD;

	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String SMTP_AUTH;

	@Value("${spring.mail.properties.mail.smtp.timeout}")
	private int SMTP_TIMEOUT;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean STARTTLS_ENABLE;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(HOST);
		mailSender.setPort(PORT);
		mailSender.setUsername(USERNAME);
		mailSender.setPassword(PASSWORD);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", SMTP_AUTH);
		props.put("mail.smtp.timeout", SMTP_TIMEOUT);
		props.put("mail.smtp.starttls.enable", STARTTLS_ENABLE);
		return mailSender;
	}
}
