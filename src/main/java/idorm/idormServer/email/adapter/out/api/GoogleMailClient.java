package idorm.idormServer.email.adapter.out.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import idorm.idormServer.email.application.port.out.SendEmailPort;
import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.domain.VerificationCode;

@Component
public class GoogleMailClient implements SendEmailPort {

	private final MailSender mailSender;
	private final String adminMail;
	private final String idormLogoImageUrl;

	public GoogleMailClient(MailSender mailSender,
		@Value("${spring.mail.username}") String adminMail,
		@Value("${s3.logo}") String idormLogoImageUrl) {
		this.mailSender = mailSender;
		this.adminMail = adminMail;
		this.idormLogoImageUrl = idormLogoImageUrl;
	}

	@Override
	@Async
	public void send(final Email email) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setTo(email.getEmail());
		mailMessage.setSubject("[idorm] 인증 코드: " + email.getCode());
		mailMessage.setText(generateEmailContent(email.getCode()));
		mailMessage.setFrom(adminMail);

		mailSender.send(mailMessage);
	}

	private String generateEmailContent(VerificationCode verificationCode) {

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
		template = template.replace("${verificationCode}", verificationCode.getValue());
		template = template.replace("${logoUrl}", idormLogoImageUrl);
		return template;
	}
}