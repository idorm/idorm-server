package idorm.idormServer.application;

import com.google.firebase.messaging.Message;
import idorm.idormServer.calendar.domain.OfficialCalendar;
import idorm.idormServer.calendar.domain.RoomMateTeamCalendar;
import idorm.idormServer.calendar.dto.CrawledOfficialCalendarResponse;
import idorm.idormServer.calendar.service.OfficialCalendarService;
import idorm.idormServer.calendar.service.RoomMateTeamCalendarService;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.fcm.dto.FcmRequest;
import idorm.idormServer.fcm.dto.NotifyType;
import idorm.idormServer.fcm.service.FCMService;
import idorm.idormServer.matching.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class Scheduler {

    private final FCMService fcmService;
    private final MemberService memberService;
    private final PostService postService;
    private final OfficialCalendarService calendarService;
    private final RoomMateTeamCalendarService teamCalendarService;
    private final OfficialCalendarCrawler officialCalendarCrawler;

    @Transactional
    @Scheduled(cron = "0 49 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:49 ASIA/SEOUL 8:49
    public void alertTopPostsAndCalendarOfDorm1() {
        List<Member> members = memberService.findAllOfDorm1();

        Post topPost = postService.findTopPost(DormCategory.DORM1);
        List<OfficialCalendar> todayCalendars = calendarService.findTodayCalendars(1);

        if (topPost == null && todayCalendars == null)
            return;

        List<FcmRequest> fcmMessages = createFcmMessages("1", topPost, todayCalendars);
        sendFcmMessage(members, fcmMessages);
    }

    @Transactional
    @Scheduled(cron = "0 52 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:52 ASIA/SEOUL 8:52
    public void alertTopPostsAndCalendarOfDorm2() {
        List<Member> members = memberService.findAllOfDorm2();

        Post topPost = postService.findTopPost(DormCategory.DORM2);
        List<OfficialCalendar> todayCalendars = calendarService.findTodayCalendars(2);

        if (topPost == null && todayCalendars == null)
            return;

        List<FcmRequest> fcmMessages = createFcmMessages("2", topPost, todayCalendars);
        sendFcmMessage(members, fcmMessages);
    }

    @Transactional
    @Scheduled(cron = "0 55 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:55 ASIA/SEOUL 8:55
    public void alertTopPostsAndCalendarOfDorm3() {
        List<Member> members = memberService.findAllOfDorm3();

        Post topPost = postService.findTopPost(DormCategory.DORM3);
        List<OfficialCalendar> todayCalendars = calendarService.findTodayCalendars(3);

        if (topPost == null && todayCalendars == null)
            return;

        List<FcmRequest> fcmMessages = createFcmMessages("3", topPost, todayCalendars);
        sendFcmMessage(members, fcmMessages);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 ? * MON,TUE,WED,THU,SUN") // UTC 00:00 ASIA/SEOUL 9:00
    public void alertTeamCalendars() {

        List<RoomMateTeamCalendar> teamCalendars = teamCalendarService.findTeamCalendarsByStartDateIsToday();

        // createFcmMessages
        List<FcmRequest> fcmRequestDtos = new ArrayList<>();

        for (RoomMateTeamCalendar teamCalendar : teamCalendars) {

            List<Long> targets = teamCalendar.getTargets(); // 일정 대상자들

            if (targets.isEmpty())
                continue;

            List<Member> teamTargetMembers = memberService.findTeamTargetMembers(targets);

            for (Member member : teamTargetMembers) {
                FcmRequest topPostFcmMessage = FcmRequest.builder()
                        .token(member.getFcmToken())
                        .notification(FcmRequest.Notification.builder()
                                .notifyType(NotifyType.TEAMCALENDAR)
                                .contentId(teamCalendar.getId())
                                .title("오늘의 팀 일정 입니다.")
                                .content(teamCalendar.getTitle())
                                .build())
                        .build();
                fcmRequestDtos.add(topPostFcmMessage);
            }
        }

        // sendFcmMessages
        List<Message> messages = fcmService.createMessageOfTeamCalendar(fcmRequestDtos);
        fcmService.sendManyMessages(messages);
    }

    private List<FcmRequest> createFcmMessages(String dormCategory, Post topPost, List<OfficialCalendar> todayCalendars) {
        List<FcmRequest> fcmMessages = new ArrayList<>();

        if (topPost != null) {
            FcmRequest topPostFcmMessage = FcmRequest.builder()
                    .notification(FcmRequest.Notification.builder()
                            .notifyType(NotifyType.TOPPOST)
                            .contentId(topPost.getId())
                            .title("어제 " + dormCategory + " 기숙사의 인기 게시글 입니다.")
                            .content(topPost.getContent())
                            .build())
                    .build();
            fcmMessages.add(topPostFcmMessage);
        }
        if (todayCalendars != null) {
            for (OfficialCalendar calendar : todayCalendars) {
                FcmRequest calendarFcmMessage = FcmRequest.builder()
                        .notification(FcmRequest.Notification.builder()
                                .notifyType(NotifyType.CALENDAR)
                                .contentId(calendar.getId())
                                .title("오늘의 " + dormCategory + " 기숙사 일정 입니다.")
                                .build())
                        .build();
                fcmMessages.add(calendarFcmMessage);
            }
        }
        return fcmMessages;
    }

    private void sendFcmMessage(List<Member> members, List<FcmRequest> fcmMessages) {
        List<Message> messages = new ArrayList<>();

        for (Member member : members) {
            if (member.getFcmTokenUpdatedAt().isBefore(LocalDate.now().minusMonths(2))) {
                memberService.deleteFcmToken(member);
                continue;
            }

            List<Message> messageList = fcmService.createMessage(member, fcmMessages);
            messages.addAll(messageList);
        }

        fcmService.sendManyMessages(messages);
    }

    @Transactional
    @Scheduled(cron = "0 0 14 ? * MON,TUE,WED,THU,FRI,SAT,SUN") // UTC 14:00 ASIA/SEOUL 23:00
    public void crawlingOfficialCalendars() {

        List<CrawledOfficialCalendarResponse> responses = officialCalendarCrawler.crawlPosts();

        // TODO: 관리자 푸시 알림 발송
    }
}
