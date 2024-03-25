package idorm.idormServer.notification.application.port.in;

import java.util.List;

import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.notification.adapter.out.event.NotificationRequest;

public interface NotificationUseCase {

	List<NotificationRequest> topPostAndOfficialCalendarsFrom(DormCategory dormCategory);

	List<NotificationRequest> teamCalendarsFrom();

	List<NotificationRequest> adminOfficialCalendarsFrom();
}
