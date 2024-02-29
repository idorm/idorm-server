package idorm.idormServer.email.adapter.out.api;

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import idorm.idormServer.email.adapter.out.exception.EmailServerErrorException;
import idorm.idormServer.email.application.port.out.SendEmailPort;
import idorm.idormServer.email.entity.Email;

@Component
public class GoogleMailClient implements SendEmailPort {

	private final JavaMailSender mailSender;
	private final String adminMail;
	private final String idormLogoImageUrl;

	public GoogleMailClient(JavaMailSender mailSender,
		@Value("${spring.mail.username}") String adminMail,
		@Value("${s3.logo}") String idormLogoImageUrl) {
		this.mailSender = mailSender;
		this.adminMail = adminMail;
		this.idormLogoImageUrl = idormLogoImageUrl;
	}

	@Override
	@Async
	public void send(final Email email) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			message.setSubject("[idorm] 인증 코드: " + email.getCode());
			message.setText(generateEmailContent(email.getCode()), "UTF-8", "html");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getEmail(), "히히", "UTF-8"));
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new EmailServerErrorException();
		}
		mailSender.send(message);
	}

	private String generateEmailContent(String verificationCode) {

		String template = "<head> " +
			"<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\" /> " +
			"<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin /> " +
			"<link href=\"https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap\" rel=\"stylesheet\" />" +
			"</head>" +
			"<body style=\" background-color: #f2f5fa; display: flex; justify-content: center; align-items: center; font-family: 'Noto Sans KR', sans-serif; \"> "
			+
			"<div style=\"width: 400px; background-color: #ffffff; padding: 70px 30px\"> <img src=\"${logoUrl}\" width=\"70\" /> "
			+
			"<p style=\" font-size: 25px; font-weight: 500; margin-top: 60px; color: #5b5b5b; \" > " +
			"<span>요청하신 </span> <span style=\"color: #71a1fe; font-weight: 700\">인증번호</span> <br />발송해드립니다. </p> " +
			"<p style=\"font-weight: 400; font-size: 20px\"> 아래 인증번호를 입력창에 입력해 주세요. </p> " +
			"<p style=\" border: 1px #e3e1ec; border-top-style: solid; padding: 25px; background-color: #f2f5fa; \" > 인증번호 <span style=\"color: #ff6868\">${verificationCode}</span> </p> </div>"
			+
			"</body>";
		template = template.replace("${verificationCode}", verificationCode);
		template = template.replace("${logoUrl}", idormLogoImageUrl);
		return template;
	}
}