package idorm.idormServer.master.develop;

import idorm.idormServer.calendar.repository.OfficialCalendarRepository;
import idorm.idormServer.calendar.repository.RoomMateTeamCalendarRepository;
import idorm.idormServer.calendar.repository.RoomMateTeamRepository;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.repository.CommentRepository;
import idorm.idormServer.community.repository.PostLikedMemberRepository;
import idorm.idormServer.community.repository.PostRepository;
import idorm.idormServer.community.service.CommentService;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.exception.ExceptionCode;
import idorm.idormServer.matching.domain.DormCategory;
import idorm.idormServer.matching.domain.Gender;
import idorm.idormServer.matching.domain.JoinPeriod;
import idorm.idormServer.matching.domain.MatchingInfo;
import idorm.idormServer.matching.repository.MatchingInfoRepository;
import idorm.idormServer.matching.service.MatchingInfoService;
import idorm.idormServer.member.domain.Email;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.EmailRepository;
import idorm.idormServer.member.repository.MemberRepository;
import idorm.idormServer.member.service.EmailService;
import idorm.idormServer.member.service.MemberService;
import idorm.idormServer.photo.repository.MemberPhotoRepository;
import idorm.idormServer.photo.repository.PostPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DevelopService {

    private final MemberService memberService;
    private final PostService postService;

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

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MatchingInfoService matchingInfoService;
    private final CommentService commentService;
    private final OfficialCalendarRepository calendarRepository;
    private final RoomMateTeamRepository teamRepository;
    private final RoomMateTeamCalendarRepository teamCalendarRepository;


    @Value("${admin.username}")
    private String id;

    @Value("${admin.password}")
    private String password;

    /**
     * 데이커베이스 초기화 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void resetDatabase() {

        try {
            calendarRepository.deleteAll();
            teamCalendarRepository.deleteAll();
            commentRepository.deleteAll();
            postLikedMemberRepository.deleteAll();
            postRepository.deleteAll();
            memberPhotoRepository.deleteAll();
            postPhotoRepository.deleteAll();
            matchingInfoRepository.deleteAll();
            emailRepository.deleteAll();
            memberRepository.deleteAll();
            teamRepository.deleteAll();

            this.entityManager
                    .createNativeQuery("ALTER TABLE official_calendar AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE room_mate_team AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE room_mate_team_calendar AUTO_INCREMENT = 1")
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
            throw new CustomException(e, ExceptionCode.SERVER_ERROR);
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
    public Member emailAndMemberDataInject(Email email, String password, String nickname) {

        Member member = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        memberService.save(member);

        email.isChecked();
        emailService.updateIsJoined(email, member);
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

