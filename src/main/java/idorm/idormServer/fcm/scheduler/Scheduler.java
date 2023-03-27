package idorm.idormServer.fcm.scheduler;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.fcm.domain.NotifyType;
import idorm.idormServer.fcm.dto.FcmRequestDto;
import idorm.idormServer.fcm.service.FCMService;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static idorm.idormServer.exception.ExceptionCode.*;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final FCMService fcmService;
    private final MemberService memberService;
    private final PostService postService;

    @Scheduled(cron = "* 55 8 * * 1,2,3,4,5")
    public void alertTopPosts() {

        List<Member> members = memberService.findAll();

        Post topPostFromDorm1 = postService.findTopPost(DormCategory.DORM1);
        Post topPostFromDorm2 = postService.findTopPost(DormCategory.DORM2);
        Post topPostFromDorm3 = postService.findTopPost(DormCategory.DORM3);

        String titleOfDorm1 = "어제 1 기숙사의 인기 게시글 입니다.";
        String titleOfDorm2 = "어제 2 기숙사의 인기 게시글 입니다.";
        String titleOfDorm3 = "어제 3 기숙사의 인기 게시글 입니다.";

        for (Member member : members) {

            if (member.getMatchingInfo() != null) {
                if (!member.getFcmTokenUpdatedAt().isBefore(LocalDate.now().minusMonths(2))) {

                    FcmRequestDto fcmRequestDto = null;
                    switch (DormCategory.valueOf(member.getMatchingInfo().getDormCategory())) {
                        case DORM1:
                            fcmRequestDto = FcmRequestDto.builder()
                                    .token(member.getFcmToken())
                                    .notification(FcmRequestDto.Notification.builder()
                                            .notifyType(NotifyType.TOPPOST)
                                            .contentId(topPostFromDorm1.getId())
                                            .title(titleOfDorm1)
                                            .content(topPostFromDorm1.getContent())
                                            .build())
                                    .build();
                            fcmService.sendMessage(fcmRequestDto);
                            break;

                        case DORM2:
                            fcmRequestDto = FcmRequestDto.builder()
                                    .token(member.getFcmToken())
                                    .notification(FcmRequestDto.Notification.builder()
                                            .notifyType(NotifyType.TOPPOST)
                                            .contentId(topPostFromDorm2.getId())
                                            .title(titleOfDorm2)
                                            .content(topPostFromDorm2.getContent())
                                            .build())
                                    .build();
                            fcmService.sendMessage(fcmRequestDto);
                            break;

                        case DORM3:
                            fcmRequestDto = FcmRequestDto.builder()
                                    .token(member.getFcmToken())
                                    .notification(FcmRequestDto.Notification.builder()
                                            .notifyType(NotifyType.TOPPOST)
                                            .contentId(topPostFromDorm3.getId())
                                            .title(titleOfDorm3)
                                            .content(topPostFromDorm3.getContent())
                                            .build())
                                    .build();
                            fcmService.sendMessage(fcmRequestDto);
                            break;
                        default:
                            throw new CustomException(null, SERVER_ERROR);
                    }
                }
            }
        }
    }
}
