package idorm.idormServer.master.develop;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.common.response.SuccessResponse;
import idorm.idormServer.notification.adapter.out.api.NotificationClient;
import idorm.idormServer.notification.adapter.out.api.NotificationRequest;
import idorm.idormServer.notification.entity.FcmChannel;
import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "0. Develop", description = "개발용 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class DevelopController {

	private final NotificationClient notificationClient;

	@Operation(summary = "fcm 토큰 발송 테스트")
	@GetMapping("/fcm/{token}")
	public ResponseEntity<SuccessResponse<Object>> sendFcmMessage(
		@PathVariable(name = "token") String token
	) {
		NotificationRequest request = NotificationRequest.builder()
			.token(token)
			.message(NotificationRequest.NotificationMessage.builder()
				.notifyType(FcmChannel.TOPPOST)
				.contentId(1L)
				.title("테스트 제목")
				.content("테스트 내용")
				.build())
			.build();

		notificationClient.notify(request);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "sentry 테스트")
	@GetMapping("/sentry")
	public ResponseEntity<Object> testSentry() {
		try {
			throw new Exception("This is a test.");
		} catch (Exception e) {
			Sentry.captureException(e);
		}
		return ResponseEntity.noContent().build();
	}
}