package idorm.idormServer.notification.adapter.out.event;

import java.util.List;

public interface NotificationClient {

	void notify(List<NotificationRequest> requests);

	void notify(NotificationRequest request);
}
