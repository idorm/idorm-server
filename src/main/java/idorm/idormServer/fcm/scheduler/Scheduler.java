package idorm.idormServer.fcm.scheduler;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.fcm.service.FCMService;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final FCMService fcmService;
    private final MemberService memberService;
    private final PostService postService;

    @Scheduled(cron = "* 55 8 * * 1,2,3,4,5")
    public void alertTopPosts() throws IOException {

        List<Member> members = memberService.findAll();
        Member admin = memberService.findById(1L);
        members.remove(admin);

        Post topPostFromDorm1 = postService.findTopPost(DormCategory.DORM1);
        Post topPostFromDorm2 = postService.findTopPost(DormCategory.DORM2);
        Post topPostFromDorm3 = postService.findTopPost(DormCategory.DORM3);

        String titleOfDorm1 = "어제 1 기숙사의 인기 게시글 입니다.";
        String titleOfDorm2 = "어제 2 기숙사의 인기 게시글 입니다.";
        String titleOfDorm3 = "어제 3 기숙사의 인기 게시글 입니다.";

        for (Member member : members) {

            if (member.getFcmToken() != null &&
                    !member.getFcmTokenUpdatedAt().isBefore(LocalDate.now().minusMonths(2))) {

                if (member.getMatchingInfo() != null) {
                    switch (DormCategory.valueOf(member.getMatchingInfo().getDormCategory())) {
                        case DORM1:
                            fcmService.sendMessage(titleOfDorm1,
                                    topPostFromDorm1.getTitle(),
                                    topPostFromDorm1.getContent());
                        case DORM2:
                            fcmService.sendMessage(titleOfDorm2,
                                    topPostFromDorm2.getTitle(),
                                    topPostFromDorm2.getContent());
                        case DORM3:
                            fcmService.sendMessage(titleOfDorm3,
                                    topPostFromDorm3.getTitle(),
                                    topPostFromDorm3.getContent());
                    }
                }
            }
        }
    }
}
