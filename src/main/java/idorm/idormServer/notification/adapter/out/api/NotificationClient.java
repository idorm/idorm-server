package idorm.idormServer.notification.adapter.out.api;

import java.util.List;

public interface NotificationClient {

	void notify(List<NotificationRequest> requests);

	void notify(NotificationRequest request);
}
