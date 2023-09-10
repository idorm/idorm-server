package idorm.idormServer.application;

import idorm.idormServer.calendar.domain.Calendar;
import idorm.idormServer.calendar.service.CalendarService;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.fcm.domain.NotifyType;
import idorm.idormServer.fcm.dto.FcmRequestDto;
import idorm.idormServer.fcm.service.FCMService;
import idorm.idormServer.matchingInfo.domain.DormCategory;
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
    private final CalendarService calendarService;

    @Transactional
    @Scheduled(cron = "0 49 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:49 ASIA/SEOUL 8:49
    public void alertTopPostsAndCalendarOfDorm1() {
        List<Member> members = memberService.findAllOfDorm1();

        Post topPost = postService.findTopPost(DormCategory.DORM1);
        List<Calendar> todayCalendars = calendarService.findTodayCalendarsFromDorm1();

        if (topPost == null && todayCalendars == null)
            return;

        List<FcmRequestDto> fcmMessages = createFcmMessages("1", topPost, todayCalendars);
        sendFcmMessage(members, fcmMessages);
    }

    @Transactional
    @Scheduled(cron = "0 52 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:52 ASIA/SEOUL 8:52
    public void alertTopPostsAndCalendarOfDorm2() {
        List<Member> members = memberService.findAllOfDorm2();

        Post topPost = postService.findTopPost(DormCategory.DORM2);
        List<Calendar> todayCalendars = calendarService.findTodayCalendarsFromDorm2();

        if (topPost == null && todayCalendars == null)
            return;

        List<FcmRequestDto> fcmMessages = createFcmMessages("2", topPost, todayCalendars);
        sendFcmMessage(members, fcmMessages);
    }

    @Transactional
    @Scheduled(cron = "0 55 23 ? * MON,TUE,WED,THU,SUN") // UTC 23:55 ASIA/SEOUL 8:55
    public void alertTopPostsAndCalendarOfDorm3() {
        List<Member> members = memberService.findAllOfDorm3();

        Post topPost = postService.findTopPost(DormCategory.DORM3);
        List<Calendar> todayCalendars = calendarService.findTodayCalendarsFromDorm3();

        if (topPost == null && todayCalendars == null)
            return;

        List<FcmRequestDto> fcmMessages = createFcmMessages("3", topPost, todayCalendars);
        sendFcmMessage(members, fcmMessages);
    }

    private List<FcmRequestDto> createFcmMessages(String dormCategory, Post topPost, List<Calendar> todayCalendars) {
        List<FcmRequestDto> fcmMessages = new ArrayList<>();

        if (topPost != null) {
            FcmRequestDto topPostFcmMessage = FcmRequestDto.builder()
                    .notification(FcmRequestDto.Notification.builder()
                            .notifyType(NotifyType.TOPPOST)
                            .contentId(topPost.getId())
                            .title("어제 " + dormCategory + " 기숙사의 인기 게시글 입니다.")
                            .content(topPost.getContent())
                            .build())
                    .build();
            fcmMessages.add(topPostFcmMessage);
        }
        if (todayCalendars != null) {
            for (Calendar calendar : todayCalendars) {
                FcmRequestDto calendarFcmMessage = FcmRequestDto.builder()
                        .notification(FcmRequestDto.Notification.builder()
                                .notifyType(NotifyType.CALENDAR)
                                .contentId(calendar.getId())
                                .title("오늘의 " + dormCategory + " 기숙사 일정 입니다.")
                                .content(calendar.getContent())
                                .build())
                        .build();
                fcmMessages.add(calendarFcmMessage);
            }
        }
        return fcmMessages;
    }

    private void sendFcmMessage(List<Member> members, List<FcmRequestDto> fcmMessages) {
        for (Member member : members) {
            if (member.getFcmTokenUpdatedAt().isBefore(LocalDate.now().minusMonths(2))) {
                memberService.deleteFcmToken(member);
                continue;
            }

            String fcmToken = member.getFcmToken();

            for (FcmRequestDto request : fcmMessages) {
                request.setToken(fcmToken);
                fcmService.sendMessage(member, request);
            }
        }
    }
}
