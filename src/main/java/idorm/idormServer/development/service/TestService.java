package idorm.idormServer.development.service;

import idorm.idormServer.calendar.domain.Calendar;
import idorm.idormServer.calendar.dto.CalendarDefaultResponseDto;
import idorm.idormServer.calendar.repository.CalendarRepository;
import idorm.idormServer.calendar.service.CalendarService;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.dto.post.PostAbstractResponseDto;
import idorm.idormServer.community.repository.CommentRepository;
import idorm.idormServer.community.repository.PostLikedMemberRepository;
import idorm.idormServer.community.repository.PostRepository;
import idorm.idormServer.community.service.CommentService;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.repository.EmailRepository;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.fcm.domain.NotifyType;
import idorm.idormServer.fcm.dto.FcmRequestDto;
import idorm.idormServer.fcm.service.FCMService;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.repository.MatchingInfoRepository;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import idorm.idormServer.member.service.MemberService;
import idorm.idormServer.photo.repository.MemberPhotoRepository;
import idorm.idormServer.photo.repository.PostPhotoRepository;
import idorm.idormServer.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final CommentRepository commentRepository;
    private final PostLikedMemberRepository postLikedMemberRepository;
    private final PostRepository postRepository;
    private final EmailRepository emailRepository;
    private final MatchingInfoRepository matchingInfoRepository;
    private final MemberRepository memberRepository;
    private final MemberPhotoRepository memberPhotoRepository;
    private final PostPhotoRepository postPhotoRepository;
    private final ReportRepository reportRepository;
    private final CalendarRepository calendarRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MemberService memberService;
    private final MatchingInfoService matchingInfoService;
    private final PostService postService;
    private final CommentService commentService;
    private final CalendarService calendarService;
    private final FCMService fcmService;

    @Value("${DB_USERNAME}")
    private String id;

    @Value("${ADMIN_PASSWORD}")
    private String password;

    @Transactional
    public void alertDorm3() {

        List<Member> members = memberService.findAllOfDorm3();

        Post topPost = postService.findTopPost(DormCategory.DORM3);
        List<Calendar> todayCalendars = calendarService.findTodayCalendarsFromDorm3();

        if (topPost == null && todayCalendars == null)
            return;

        List<FcmRequestDto> fcmMessages = createFcmMessages("3", topPost, todayCalendars);
        sendFcmMessage(members, fcmMessages);
    }

    public List<FcmRequestDto> createFcmMessages(String dormCategory, Post topPost, List<Calendar> todayCalendars) {
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

    public void sendFcmMessage(List<Member> members, List<FcmRequestDto> fcmMessages) {
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

    /**
     * 3 기숙사 오늘의 일정 조회 |
     */
    public List<CalendarDefaultResponseDto> findTodayCalendarOfDorm3() {

        List<Calendar> calendars = calendarService.findTodayCalendarsFromDorm3();

        return calendars.stream()
                .map(c -> new CalendarDefaultResponseDto(c)).collect(Collectors.toList());
    }

    /**
     * 데이커베이스 초기화 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void resetDatabase() {

        try {
            commentRepository.deleteAll();
            postLikedMemberRepository.deleteAll();
            memberPhotoRepository.deleteAll();
            postPhotoRepository.deleteAll();
            postRepository.deleteAll();
            matchingInfoRepository.deleteAll();
            emailRepository.deleteAll();
            reportRepository.deleteAll();
            calendarRepository.deleteAll();
            memberRepository.deleteAll();

            this.entityManager
                    .createNativeQuery("ALTER TABLE calendar AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE report AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE comment AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE post_photo AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE post_liked_member AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE post AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE member_photo AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE matching_info AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE email AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE member AUTO_INCREMENT = 1")
                    .executeUpdate();

            injectTestData();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    public void injectTestData() {
        createAdmin();
        List<Member> members = new ArrayList<>();
        List<Post> posts = new ArrayList<>();

        /**
         * 이메일 / 회원 / 매칭정보 주입
         */
        for (int i = 1; i <= 100; i++) {
            String emailString = "test" + i + "@inu.ac.kr";
            Email email = emailInject(emailString);

            Member member = memberDataInject(email,
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

        Email email = Email.builder()
                .email(id + "@inu.ac.kr")
                .code("111-111")
                .build();

        emailService.save(email);

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname("ADMIN")
                .build();

        member.getRoles().clear();
        member.getRoles().add("ROLE_ADMIN");

        memberService.save(member);

        email.isChecked();
        emailService.updateIsJoined(email, member);
    }

    @Transactional
    public Email emailInject(String email) {
        Email createdEmail = Email.builder()
                .email(email)
                .code("111-111")
                .build();
        return emailService.save(createdEmail);
    }

    @Transactional
    public Member memberDataInject(Email email, String password, String nickname) {

        Member member = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        memberService.save(member);

        email.isChecked();
        emailService.updateIsJoined(email, member);
        memberService.updateFcmToken(member, "f2uPzBhiTm-peoTQdXOzF1:APA91bEzJTtxjy7AeMaY_rCJ9jKSC2Br4-209FnNM3HuuYAOGf6KvMpWY-S5ahHHza-U5BaR40gd2dhUt91DjR1IZFJTGweQ57jB04AxVaytkbs-pChiItDrIFJQmNS6dYpMl0T291TS");
        return member;
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
        matchingInfoService.updateMatchingInfoIsPublic(matchingInfo, true);
        matchingInfoService.save(matchingInfo);

        matchingInfo.getMember().updateDormCategory(matchingInfo.getDormCategory());
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

        return postService.save(post);
    }

    @Transactional
    public Comment commentDataInject(String content, Boolean isAnonymous, Post post, Member member) {

        Comment comment = Comment.builder()
                .content(content)
                .isAnonymous(isAnonymous)
                .post(post)
                .member(member)
                .build();

        return commentService.save(comment);
    }
}
