package idorm.idormServer.development.inject;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.service.CommentService;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInjector implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MemberService memberService;
    private final MatchingInfoService matchingInfoService;
    private final PostService postService;
    private final CommentService commentService;

    @Value("${DB_USERNAME}")
    private String id;

    @Value("${ADMIN_PASSWORD}")
    private String password;

    @Override
    public void run(ApplicationArguments args) {

        createAdmin();
        List<Member> members = new ArrayList<>();
        List<Post> posts = new ArrayList<>();

        /**
         * 이메일 / 회원 / 매칭정보 주입
         */
        for (int i = 1; i <= 100; i++) {
            String email = "test" + i + "@inu.ac.kr";
            Member member = emailAndMemberDataInject(email,
                    passwordEncoder.encode("idorm2023!"),
                    "응철이" + i);
            members.add(member);
        }

        for (int i = 0; i<= 99; i++) {
            if (i <= 50) {
                if (i % 3 == 0) {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM1,
                            JoinPeriod.WEEK24,
                            Gender.MALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            false,
                            false,
                            false,
                            true,
                            "저는 특별해요..⭐️",
                            "쪼끔 더러워요..",
                            "잘 안 씻어요.",
                            "INFP",
                            "응철이 구해요.",
                            "https://open.kakao.com/o/szaIN6ze");
                } else if (i % 5 == 0) {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM1,
                            JoinPeriod.WEEK16,
                            Gender.FEMALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            false,
                            false,
                            false,
                            true,
                            "적당함",
                            "적당함",
                            "불규칙적",
                            "INFJ",
                            "적당한 룸메이트 구해요",
                            "https://open.kakao.com/o/szaIN6ze");
                } else if (i % 2 == 0) {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM1,
                            JoinPeriod.WEEK24,
                            Gender.FEMALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            false,
                            false,
                            false,
                            true,
                            "일해라 일!! 부지런해요",
                            "완.벽.주.의.",
                            "새벽 6시, 밤 11시에 씻어요.",
                            "ENFJ",
                            "게으른 사람 싫어요!",
                            "https://open.kakao.com/o/szaIN6ze");
                } else {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM1,
                            JoinPeriod.WEEK16,
                            Gender.MALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            false,
                            false,
                            false,
                            true,
                            "평생 잠만 자고 싶어요..",
                            "적당함",
                            "불규칙적",
                            "ISTP",
                            "룸메이트 구해요",
                            "https://open.kakao.com/o/szaIN6ze");
                }
            } else if (i >= 51 && i <= 70) {
                if (i % 3 == 0) {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM2,
                            JoinPeriod.WEEK24,
                            Gender.MALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            false,
                            false,
                            true,
                            true,
                            "저는 특별해요..⭐️",
                            "쪼끔 더러워요..",
                            "잘 안 씻어요.",
                            "INFP",
                            "응철이 구해요.",
                            "https://open.kakao.com/o/szaIN6ze");
                } else if (i % 5 == 0) {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM2,
                            JoinPeriod.WEEK16,
                            Gender.FEMALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            true,
                            false,
                            false,
                            true,
                            "적당함",
                            "적당함",
                            "불규칙적",
                            "INFJ",
                            "적당한 룸메이트 구해요",
                            "https://open.kakao.com/o/szaIN6ze");
                } else if (i % 2 == 0) {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM2,
                            JoinPeriod.WEEK24,
                            Gender.FEMALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            false,
                            true,
                            false,
                            true,
                            "일해라 일!! 부지런해요",
                            "완.벽.주.의.",
                            "새벽 6시, 밤 11시에 씻어요.",
                            "ENFJ",
                            "게으른 사람 싫어요!",
                            "https://open.kakao.com/o/szaIN6ze");
                } else {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM2,
                            JoinPeriod.WEEK16,
                            Gender.MALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            false,
                            false,
                            false,
                            true,
                            "평생 잠만 자고 싶어요..",
                            "적당함",
                            "불규칙적",
                            "ISTP",
                            "룸메이트 구해요",
                            "https://open.kakao.com/o/szaIN6ze");
                }
            } else if (i >= 71 && i <= 99) {
                if (i % 3 == 0) {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM3,
                            JoinPeriod.WEEK24,
                            Gender.MALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            false,
                            false,
                            true,
                            true,
                            "저는 특별해요..⭐️",
                            "쪼끔 더러워요..",
                            "잘 안 씻어요.",
                            "INFP",
                            "응철이 구해요.",
                            "https://open.kakao.com/o/szaIN6ze");
                } else if (i % 5 == 0) {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM3,
                            JoinPeriod.WEEK16,
                            Gender.FEMALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            true,
                            false,
                            false,
                            true,
                            "적당함",
                            "적당함",
                            "불규칙적",
                            "INFJ",
                            "적당한 룸메이트 구해요",
                            "https://open.kakao.com/o/szaIN6ze");
                } else if (i % 2 == 0) {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM3,
                            JoinPeriod.WEEK24,
                            Gender.FEMALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            false,
                            true,
                            false,
                            true,
                            "일해라 일!! 부지런해요",
                            "완.벽.주.의.",
                            "새벽 6시, 밤 11시에 씻어요.",
                            "ENFJ",
                            "게으른 사람 싫어요!",
                            "https://open.kakao.com/o/szaIN6ze");
                } else {
                    matchingInfoDataInject(members.get(i),
                            DormCategory.DORM3,
                            JoinPeriod.WEEK16,
                            Gender.MALE,
                            Integer.valueOf(2 + String.valueOf(i % 10)),
                            false,
                            false,
                            false,
                            false,
                            true,
                            "평생 잠만 자고 싶어요..",
                            "적당함",
                            "불규칙적",
                            "ISTP",
                            "룸메이트 구해요",
                            "https://open.kakao.com/o/szaIN6ze");
                }
            }
        }



        /**
         * 게시글 주입
         */
        for (int i = 0; i <= 99; i++) {
            if (i <= 30 && (i % 5 == 0)) {
                Post post = postDataInject(members.get(0),
                        DormCategory.DORM1,
                        "테스트 제목 " + i,
                        "테스트 내용 " + i,
                        false);
                posts.add(post);
            } else if (i <= 30 && (i % 3 == 0) && i != 18) {
                Post post = postDataInject(members.get(18),
                        DormCategory.DORM1,
                        "테스트 제목 " + i,
                        "테스트 내용 " + i,
                        false);
                posts.add(post);
            } else if (i <= 30) {
                Post post = postDataInject(members.get(i),
                        DormCategory.DORM1,
                        "테스트 제목 " + i,
                        "테스트 내용 " + i,
                        true);
                posts.add(post);
            } else if (i <= 60 && (i % 5 == 0)) {
                Post post = postDataInject(members.get(i),
                        DormCategory.DORM2,
                        "테스트 제목 " + i,
                        "테스트 내용 " + i,
                        false);
                posts.add(post);
            } else if (i <= 60 && (i % 3 == 0)) {
                Post post = postDataInject(members.get(i),
                        DormCategory.DORM2,
                        "테스트 제목 " + i,
                        "테스트 내용 " + i,
                        false);
                posts.add(post);
            } else if (i <= 60) {
                Post post = postDataInject(members.get(i),
                        DormCategory.DORM2,
                        "테스트 제목 " + i,
                        "테스트 내용 " + i,
                        true);
                posts.add(post);
            } else {
                Post post = postDataInject(members.get(i),
                        DormCategory.DORM3,
                        "테스트 제목 " + i,
                        "테스트 내용 " + i,
                        true);
                posts.add(post);
            }
        }

        /**
         * 댓글 주입
         */
        for (int i = 0; i <= 99; i++) {
            if (i <= 30 && (i % 4) == 0) {
                Comment comment = commentDataInject("테스트 댓글 내용 " + i,
                        false,
                        posts.get(0),
                        members.get(i));
            } else if (i <= 50 && (i % 5) == 0) {
                Comment comment = commentDataInject("테스트 댓글 내용 " + i,
                        false,
                        posts.get(i % 10),
                        members.get(i % 10));
            } else if (i <= 70 && (i % 7) == 0) {
                Comment comment = commentDataInject("테스트 댓글 내용 " + i,
                        false,
                        posts.get(i % 10),
                        members.get(i % 10));
            } else {
                Comment comment = commentDataInject("테스트 댓글 내용 " + i,
                        false,
                        posts.get(i % 10),
                        members.get(i % 10));

                if (i <= 70) {
                    commentService.saveParentCommentId(Long.valueOf(i), comment);
                } else {
                    commentService.saveParentCommentId(Long.valueOf(i - 30), comment);
                }
            }
        }
    }

    @Transactional
    public void createAdmin() {
        Member member = Member.builder()
                .email(id + "@inu.ac.kr")
                .password(passwordEncoder.encode(password))
                .nickname("ADMIN")
                .build();

        member.getRoles().clear();
        member.getRoles().add("ROLE_ADMIN");

        memberService.save(member);
    }

    @Transactional
    public Member emailAndMemberDataInject(String email, String password, String nickname) {

        emailService.save(email, "111-111");

        Member member = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
        return memberService.save(member);
    }

    @Transactional
    public void matchingInfoDataInject(Member member,
                                       DormCategory dormCategory,
                                       JoinPeriod joinPeriod,
                                       Gender gender,
                                       Integer age,
                                       Boolean isSnoring,
                                       Boolean isSmoking,
                                       Boolean isGrinding,
                                       Boolean isWearEarphones,
                                       Boolean isAllowedFood,
                                       String wakeUpTime,
                                       String cleanUpStatus,
                                       String showerTime,
                                       String mbti,
                                       String wishText,
                                       String openKakaoLink) {

        MatchingInfo matchingInfo = MatchingInfo.builder()
                .member(member)
                .dormCategory(dormCategory)
                .joinPeriod(joinPeriod)
                .gender(gender)
                .age(age)
                .isAllowedFood(isAllowedFood)
                .isGrinding(isGrinding)
                .isSmoking(isSmoking)
                .isSnoring(isSnoring)
                .isWearEarphones(isWearEarphones)
                .cleanUpStatus(cleanUpStatus)
                .showerTime(showerTime)
                .wakeUpTime(wakeUpTime)
                .wishText(wishText)
                .mbti(mbti)
                .openKakaoLink(openKakaoLink)
                .build();
        matchingInfoService.save(matchingInfo);
        matchingInfoService.updateMatchingInfoIsPublic(matchingInfo, true);
    }

    @Transactional
    public Post postDataInject(Member member,
                               DormCategory dormCategory,
                               String title,
                               String content,
                               Boolean isAnonymous) {

        Post post = Post.builder()
                .member(member)
                .dormCategory(dormCategory)
                .title(title)
                .content(content)
                .isAnonymous(isAnonymous)
                .build();

        return postService.save(member, post);
    }

    @Transactional
    public Comment commentDataInject(String content, Boolean isAnonymous, Post post, Member member) {

        Comment comment = Comment.builder()
                .content(content)
                .isAnonymous(isAnonymous)
                .post(post)
                .member(member)
                .build();

        return commentService.save(member, comment);
    }
}