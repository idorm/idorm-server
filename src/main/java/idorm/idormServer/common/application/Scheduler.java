package idorm.idormServer.common.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.calendar.application.port.out.FindInuPost;
import idorm.idormServer.calendar.application.port.out.LoadOfficialCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamCalendarPort;
import idorm.idormServer.calendar.entity.OfficialCalendar;
import idorm.idormServer.calendar.entity.Participant;
import idorm.idormServer.calendar.entity.TeamCalendar;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.entity.Member;
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
		List<Member> admins = loadMemberPort.loadAdmins();

		List<NotificationRequest> requests = newPosts.stream()
			.map(calendar -> adminOfficialCalendarNotificationOf(calendar, admins))
			.flatMap(List::stream)
			.toList();
		notificationClient.notify(requests);
	}

	@Transactional
	@Scheduled(cron = "0 0 14 ? * SUN") // UTC 14:00 ASIA/SEOUL 23:00
	public void manageFcmToken() {
		deleteFcmTokenPort.deleteInactiveUserTokens();
	}

	private List<NotificationRequest> notificationRequestsFrom(DormCategory dormCategory) {
		Post topPost = loadPostPort.findTopPostByDormCategory(dormCategory);
		List<OfficialCalendar> officialCalendars = loadOfficialCalendarPort.findByToday(dormCategory);

		if (topPost == null && (Objects.isNull(officialCalendars) || officialCalendars.isEmpty())) {
			return null;
		}

		List<Member> members = loadMemberPort.loadMembersBy(dormCategory);
		List<NotificationRequest> notificationRequests = new ArrayList<>();
		if (topPost != null) {
			List<NotificationRequest> topPostRequests = members.stream()
				.map(member -> topPostNotificationOf(member, topPost))
				.toList();
			notificationRequests.addAll(topPostRequests);
		}
		if (Objects.isNull(officialCalendars) || officialCalendars.isEmpty()) {
			List<NotificationRequest> officialCalendarRequests = members.stream()
				.map(member -> officialCalnedarNotificationOf(member, officialCalendars))
				.flatMap(List::stream)
				.toList();
			notificationRequests.addAll(officialCalendarRequests);
		}
		return notificationRequests;
	}

	private NotificationRequest topPostNotificationOf(Member target, Post post) {
		Optional<FcmToken> fcmToken = loadFcmTokenPort.load(target.getId());
		if (fcmToken.isEmpty()) {
			return null;
		}

		NotificationRequest request = NotificationRequest.builder()
			.token(fcmToken.get().getValue())
			.message(NotificationRequest.NotificationMessage.builder()
				.notifyType(FcmChannel.TOPPOST)
				.contentId(post.getId())
				.title(FcmChannel.TOPPOST.getTitle())
				.content(post.getTitle())
				.build())
			.build();
		return request;
	}

	private List<NotificationRequest> officialCalnedarNotificationOf(Member target, List<OfficialCalendar> calendars) {
		Optional<FcmToken> fcmToken = loadFcmTokenPort.load(target.getId());
		if (fcmToken.isEmpty()) {
			return null;
		}
		String token = fcmToken.get().getValue();

		List<NotificationRequest> requests = calendars.stream()
			.map(calendar -> {
				NotificationRequest request = NotificationRequest.builder()
					.token(token)
					.message(NotificationRequest.NotificationMessage.builder()
						.notifyType(FcmChannel.CALENDAR)
						.contentId(calendar.getId())
						.title(FcmChannel.CALENDAR.getTitle())
						.content(calendar.getTitle())
						.build())
					.build();
				return request;
			})
			.toList();
		return requests;
	}

	private List<NotificationRequest> teamCalendarNotificationOf(TeamCalendar teamCalendar) {
		List<Participant> participants = teamCalendar.getParticipants();
		List<NotificationRequest> requests = participants.stream()
			.map(participant -> {
				Optional<FcmToken> fcmToken = loadFcmTokenPort.load(participant.getMemberId());
				if (fcmToken.isEmpty())
					return null;

				NotificationRequest request = NotificationRequest.builder()
					.token(fcmToken.get().getValue())
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

	private List<NotificationRequest> adminOfficialCalendarNotificationOf(OfficialCalendar officialCalendar,
		List<Member> members) {
		List<NotificationRequest> requests = members.stream()
			.map(member -> {
				Optional<FcmToken> fcmToken = loadFcmTokenPort.load(member.getId());
				if (fcmToken.isEmpty())
					return null;

				NotificationRequest request = NotificationRequest.builder()
					.token(fcmToken.get().getValue())
					.message(NotificationRequest.NotificationMessage.builder()
						.notifyType(FcmChannel.CALENDAR)
						.contentId(officialCalendar.getId())
						.title(FcmChannel.CALENDAR.getTitle())
						.content(officialCalendar.getTitle())
						.build())
					.build();
				return request;
			})
			.toList();
		return requests;
	}
}
