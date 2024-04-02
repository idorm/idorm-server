package idorm.idormServer.notification.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.notification.adapter.out.api.NotificationClient;
import idorm.idormServer.notification.application.port.in.FcmTokenUseCase;
import idorm.idormServer.notification.application.port.in.NotificationUseCase;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Scheduler {

	private final NotificationClient notificationClient;
	private final NotificationUseCase notificationUseCase;
	private final FcmTokenUseCase fcmTokenUseCase;

	@Scheduled(cron = "0 49 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:49 ASIA/SEOUL 8:49
	public void alertTopPostAndOfficialCalendarsOfDorm1() {
		notificationClient.notify(notificationUseCase.topPostAndOfficialCalendarsFrom(DormCategory.DORM1));
	}

	@Scheduled(cron = "0 52 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:52 ASIA/SEOUL 8:52
	public void alertTopPostAndOfficialCalendarsOfDorm2() {
		notificationClient.notify(notificationUseCase.topPostAndOfficialCalendarsFrom(DormCategory.DORM2));
	}

	@Scheduled(cron = "0 55 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:55 ASIA/SEOUL 8:55
	public void alertTopPostsAndCalendarOfDorm3() {
		notificationClient.notify(notificationUseCase.topPostAndOfficialCalendarsFrom(DormCategory.DORM3));
	}

	@Scheduled(cron = "0 0 0 ? * MON,TUE,WED,THU,SUN") // UTC 00:00 ASIA/SEOUL 9:00
	public void alertTeamCalendars() {
		notificationClient.notify(notificationUseCase.teamCalendarsFrom());
	}

	@Scheduled(cron = "0 0 14 ? * MON,TUE,WED,THU,FRI,SAT,SUN") // UTC 14:00 ASIA/SEOUL 23:00
	public void alertAdminOfOfficialCalendars() {
		notificationClient.notify(notificationUseCase.adminOfficialCalendarsFrom());
	}

	@Scheduled(cron = "0 0 14 ? * SUN") // UTC 14:00 ASIA/SEOUL 23:00
	public void manageFcmToken() {
		//TODO: 리프레시 토큰이 만료된 회원의 fcm 토큰을 만료시킨다.
		fcmTokenUseCase.expireOfExpiredMembers();
	}
}
