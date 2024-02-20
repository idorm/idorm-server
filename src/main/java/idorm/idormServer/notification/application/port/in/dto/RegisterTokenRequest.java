package idorm.idormServer.notification.application.port.in.dto;

public record RegisterTokenRequest(
	Long memberId,
	String fcmToken
) {
}
