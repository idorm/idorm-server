package idorm.idormServer.notification.adapter.out.api;

import idorm.idormServer.notification.entity.FcmChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class NotificationRequest {

	@Setter
	private String token;
	private NotificationMessage message;

	@AllArgsConstructor
	@Builder
	public static class NotificationMessage {

		private FcmChannel notifyType;
		private Long contentId;

		@Getter
		private String title;
		@Getter
		private String content;

		public String getNotifyType() {
			return notifyType.name();
		}

		public String getContentId() {
			return contentId.toString();
		}
	}
}
