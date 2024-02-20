package idorm.idormServer.notification.adapter.out.api;

import idorm.idormServer.notification.domain.FcmChannel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class NotificationRequest {

	@Setter
	private String token;
	private NotificationMessage message;

	@Getter
	@Builder
	public class NotificationMessage {

		private FcmChannel notifyType;

		private Long contentId;

		private String title;

		private String content;
	}
}
