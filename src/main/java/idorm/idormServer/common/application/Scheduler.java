package idorm.idormServer.common.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.calendar.application.port.out.FindInuPost;
import idorm.idormServer.calendar.application.port.out.LoadOfficialCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamCalendarPort;
import idorm.idormServer.calendar.entity.OfficialCalendar;
import idorm.idormServer.calendar.entity.TeamCalendar;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.notification.adapter.out.event.NotificationClient;
import idorm.idormServer.notification.adapter.out.event.NotificationRequest;
import idorm.idormServer.notification.application.port.out.DeleteFcmTokenPort;
import idorm.idormServer.notification.application.port.out.LoadFcmTokenPort;
import idorm.idormServer.notification.entity.FcmChannel;
import idorm.idormServer.notification.entity.FcmToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Scheduler {

	private final LoadFcmTokenPort loadFcmTokenPort;
	private final NotificationClient notificationClient;

	private final FindInuPost findInuPost;

	private final LoadMemberPort loadMemberPort;
	private final LoadPostPort loadPostPort;
	private final LoadOfficialCalendarPort loadOfficialCalendarPort;
	private final LoadTeamCalendarPort loadTeamCalendarPort;

	private final DeleteFcmTokenPort deleteFcmTokenPort;

	@Transactional
	@Scheduled(cron = "0 49 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:49 ASIA/SEOUL 8:49
	public void alertTopPostAndOfficialCalendarsOfDorm1() {
		notificationClient.notify(notificationRequestsFrom(DormCategory.DORM1));
	}

	@Transactional
	@Scheduled(cron = "0 52 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:52 ASIA/SEOUL 8:52
	public void alertTopPostAndOfficialCalendarsOfDorm2() {
		notificationClient.notify(notificationRequestsFrom(DormCategory.DORM2));
	}

	@Transactional
	@Scheduled(cron = "0 55 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:55 ASIA/SEOUL 8:55
	public void alertTopPostsAndCalendarOfDorm3() {
		notificationClient.notify(notificationRequestsFrom(DormCategory.DORM3));
	}

	@Transactional
	@Scheduled(cron = "0 0 0 ? * MON,TUE,WED,THU,SUN") // UTC 00:00 ASIA/SEOUL 9:00
	public void alertTeamCalendars() {
		List<TeamCalendar> teamCalendars = loadTeamCalendarPort.findByStartDateIsToday();
		List<NotificationRequest> requests = teamCalendars.stream()
			.map(this::teamCalendarNotificationOf)
			.flatMap(List::stream)
			.toList();

		notificationClient.notify(requests);
	}

	@Transactional
	@Scheduled(cron = "0 0 14 ? * MON,TUE,WED,THU,FRI,SAT,SUN") // UTC 14:00 ASIA/SEOUL 23:00
	public void alertAdminOfOfficialCalendars() {
		List<OfficialCalendar> newPosts = findInuPost.findNewPosts();
		List<FcmToken> fcmTokens = loadFcmTokenPort.loadAllByAdmins();

		List<NotificationRequest> requests = newPosts.stream()
			.flatMap(calendar -> fcmTokens.stream().map(fcmToken -> {
				NotificationRequest request = NotificationRequest.builder()
					.token(fcmToken.getValue())
					.message(NotificationRequest.NotificationMessage.builder()
						.notifyType(FcmChannel.CALENDAR)
						.contentId(calendar.getId())
						.title(FcmChannel.CALENDAR.getTitle())
						.content(calendar.getTitle())
						.build())
					.build();
				return request;
			}))
			.toList();

		notificationClient.notify(requests);
	}

	@Transactional
	@Scheduled(cron = "0 0 14 ? * SUN") // UTC 14:00 ASIA/SEOUL 23:00
	public void manageFcmToken() {
		deleteFcmTokenPort.deleteInactiveUserTokens();
	}

	private List<NotificationRequest> notificationRequestsFrom(DormCategory dormCategory) {
		final Post topPost = loadPostPort.findTopPostByDormCategory(dormCategory);
		final List<OfficialCalendar> officialCalendars = loadOfficialCalendarPort.findByToday(dormCategory);

		if (topPost == null && officialCalendars.isEmpty()) {
			return Collections.emptyList();
		}

		List<FcmToken> fcmTokens = loadFcmTokenPort.loadAllByDormCategory(dormCategory);
		List<NotificationRequest> notificationRequests = new ArrayList<>();

		notificationRequests.addAll(topPostNotificationOf(fcmTokens, topPost));
		notificationRequests.addAll(todayOfficialCalendarNotificationOf(fcmTokens, officialCalendars));

		return notificationRequests;
	}

	private List<NotificationRequest> topPostNotificationOf(List<FcmToken> fcmTokens, Post topPost) {
		if (topPost == null) {
			return Collections.emptyList();
		}

		List<NotificationRequest> topPostRequests = fcmTokens.stream()
			.map(fcmToken -> {
				NotificationRequest request = NotificationRequest.builder()
					.token(fcmToken.getValue())
					.message(NotificationRequest.NotificationMessage.builder()
						.notifyType(FcmChannel.TOPPOST)
						.contentId(topPost.getId())
						.title(FcmChannel.TOPPOST.getTitle())
						.content(topPost.getTitle())
						.build())
					.build();
				return request;
			})
			.toList();
		return topPostRequests;
	}

	private List<NotificationRequest> todayOfficialCalendarNotificationOf(List<FcmToken> fcmTokens,
		List<OfficialCalendar> officialCalendars) {
		if (officialCalendars.isEmpty()) {
			return Collections.emptyList();
		}

		List<NotificationRequest> officialCalendarRequests = fcmTokens.stream()
			.flatMap(fcmToken -> officialCalendars.stream().map(calendar -> {
				NotificationRequest request = NotificationRequest.builder()
					.token(fcmToken.getValue())
					.message(NotificationRequest.NotificationMessage.builder()
						.notifyType(FcmChannel.CALENDAR)
						.contentId(calendar.getId())
						.title(FcmChannel.CALENDAR.getTitle())
						.content(calendar.getTitle())
						.build())
					.build();
				return request;
			}))
			.toList();
		return officialCalendarRequests;
	}

	private List<NotificationRequest> teamCalendarNotificationOf(TeamCalendar teamCalendar) {
		List<FcmToken> fcmTokens = loadFcmTokenPort.loadAllByMemberIds(teamCalendar.getParticipantMemberIds());

		List<NotificationRequest> requests = fcmTokens.stream()
			.map(fcmToken -> {
				NotificationRequest request = NotificationRequest.builder()
					.token(fcmToken.getValue())
					.message(NotificationRequest.NotificationMessage.builder()
						.notifyType(FcmChannel.TEAMCALENDAR)
						.contentId(teamCalendar.getId())
						.title(FcmChannel.TEAMCALENDAR.getTitle())
						.content(teamCalendar.getTitle())
						.build())
					.build();
				return request;
			})
			.toList();
		return requests;
	}
}
