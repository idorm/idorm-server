package idorm.idormServer.notification.application;

import static org.springframework.util.StringUtils.isEmpty;

import idorm.idormServer.calendar.application.port.out.LoadOfficialCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadOfficialPost;
import idorm.idormServer.calendar.application.port.out.LoadTeamCalendarPort;
import idorm.idormServer.calendar.entity.OfficialCalendar;
import idorm.idormServer.community.post.application.port.in.dto.querydsl.PostSearchResponse;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.notification.adapter.out.api.NotificationRequest;
import idorm.idormServer.notification.application.port.in.NotificationUseCase;
import idorm.idormServer.notification.application.port.out.LoadFcmTokenPort;
import idorm.idormServer.notification.entity.FcmChannel;
import idorm.idormServer.notification.entity.FcmToken;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService implements NotificationUseCase {

    private final LoadFcmTokenPort loadFcmTokenPort;
    private final LoadPostPort loadPostPort;
    private final LoadOfficialCalendarPort loadOfficialCalendarPort;
    private final LoadOfficialPost loadOfficialPost;
    private final LoadTeamCalendarPort loadTeamCalendarPort;

    @Override
    public List<NotificationRequest> topPostAndOfficialCalendarsFrom(DormCategory dormCategory) {
        final PostSearchResponse topPost = loadPostPort.findTopPostByDormCategory(dormCategory);
        final List<OfficialCalendar> officialCalendars = loadOfficialCalendarPort.findByToday(dormCategory);

        if (isEmpty(topPost) & officialCalendars.isEmpty()) {
            return Collections.emptyList();
        }

        List<FcmToken> fcmTokens = loadFcmTokenPort.loadAllByDormCategory(dormCategory);
        List<NotificationRequest> notificationRequests = new ArrayList<>();

        notificationRequests.addAll(topPostOf(fcmTokens, topPost));
        notificationRequests.addAll(officialCalendarsOf(fcmTokens, officialCalendars));

        return notificationRequests;
    }

    @Override
    public List<NotificationRequest> teamCalendarsFrom() {
        return loadTeamCalendarPort.findByStartDateIsToday();
    }

    @Override
    public List<NotificationRequest> adminOfficialCalendarsFrom() {
        List<OfficialCalendar> newPosts = loadOfficialPost.findNewPosts();
        List<FcmToken> fcmTokens = loadFcmTokenPort.loadAllByAdmins();

        return newPosts.stream()
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
    }

    private List<NotificationRequest> topPostOf(List<FcmToken> fcmTokens, PostSearchResponse topPost) {
        if (topPost == null) {
            return Collections.emptyList();
        }

        List<NotificationRequest> topPostRequests = fcmTokens.stream()
            .map(fcmToken -> {
                NotificationRequest request = NotificationRequest.builder()
                    .token(fcmToken.getValue())
                    .message(NotificationRequest.NotificationMessage.builder()
                        .notifyType(FcmChannel.TOPPOST)
                        .contentId(topPost.postId())
                        .title(FcmChannel.TOPPOST.getTitle())
                        .content(topPost.title())
                        .build())
                    .build();
                return request;
            })
            .toList();
        return topPostRequests;
    }

    private List<NotificationRequest> officialCalendarsOf(List<FcmToken> fcmTokens,
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
}
