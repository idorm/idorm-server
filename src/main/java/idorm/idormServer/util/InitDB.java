package idorm.idormServer.util;

import idorm.idormServer.email.domain.Email;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct // 스프링빈이 다 올라오면 스프링이 호출
    public void init() {
        // 여기에 InitService를 넣어줘도 되지만 굳이 transaction을 따로 먹여서 호출하는 이유는
        // spring life cycle이 있어서 transaction 등이 잘 작동이 안됨. 그래서 별도의 bean으로 등록하고 호출하는 방식으로 사용하는 것을 권장
         initService.dbInit1();
        initService.dbInit3();
         initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Value("${DB_USERNAME}")
        private String id;

        @Value("${DB_PASSWORD}")
        private String password;

        public void dbInit1() {
            String adminEmail = id;
            Member admin = createMember(adminEmail, passwordEncoder.encode(password), "idorm");
            em.persist(admin);

            admin.getRoles().clear();

            admin.getRoles().add("ROLE_ADMIN");
        }

        public void dbInit2() {
            String nickname = "응철이";
            Member member1 = createMember("aaa@inu.ac.kr", passwordEncoder.encode("aaa"), "자고있는 " + nickname);
            Member member2 = createMember("bbb@inu.ac.kr", passwordEncoder.encode("bbb"), "깨어있는 " + nickname);
            Member member3 = createMember("ccc@inu.ac.kr", passwordEncoder.encode("ccc"), "졸고있는 " + nickname);
            Member member4 = createMember("ddd@inu.ac.kr", passwordEncoder.encode("ddd"), "눈비비는 " + nickname);
            Member member5 = createMember("eee@inu.ac.kr", passwordEncoder.encode("eee"), "팔공티 먹는 " + nickname);

            Member member6 = createMember("fff@inu.ac.kr", passwordEncoder.encode("eee"), "스타벅스 먹는 " + nickname);
            Member member7 = createMember("ggg@inu.ac.kr", passwordEncoder.encode("eee"), "코딩하는 " + nickname);
            Member member8 = createMember("hhh@inu.ac.kr", passwordEncoder.encode("eee"), "투썸이 좋은 " + nickname);
            Member member9 = createMember("iii@inu.ac.kr", passwordEncoder.encode("eee"), "코딩이 좋은 " + nickname);
            Member member10 = createMember("jjj@inu.ac.kr", passwordEncoder.encode("eee"), "노래 부르는 " + nickname);
            Member member11 = createMember("kkk@inu.ac.kr", passwordEncoder.encode("eee"), "간식 먹는 " + nickname);
            Member member12 = createMember("lll@inu.ac.kr", passwordEncoder.encode("eee"), "집 가고싶은 " + nickname);
            Member member13 = createMember("mmm@inu.ac.kr", passwordEncoder.encode("eee"), "나는 " + nickname);
            Member member14 = createMember("nnn@inu.ac.kr", passwordEncoder.encode("eee"), "너도 " + nickname);
            Member member15 = createMember("ooo@inu.ac.kr", passwordEncoder.encode("eee"), "우리는 " + nickname);
            Member member16 = createMember("ppp@inu.ac.kr", passwordEncoder.encode("eee"), "따뜻한 " + nickname);
            Member member17 = createMember("qqq@inu.ac.kr", passwordEncoder.encode("eee"), "삐진 " + nickname);
            Member member18 = createMember("rrr@inu.ac.kr", passwordEncoder.encode("eee"), "화내는 " + nickname);
            Member member19 = createMember("sss@inu.ac.kr", passwordEncoder.encode("eee"), "더운 " + nickname);
            Member member20 = createMember("ttt@inu.ac.kr", passwordEncoder.encode("eee"), "크아하는 " + nickname);
            Member member21 = createMember("uuu@inu.ac.kr", passwordEncoder.encode("eee"), "굴러 떨어진 " + nickname);
            Member member22 = createMember("vvv@inu.ac.kr", passwordEncoder.encode("eee"), "탈주하고 싶은 " + nickname);
            Member member23 = createMember("www@inu.ac.kr", passwordEncoder.encode("eee"), "울상 짓는 " + nickname);
            Member member24 = createMember("xxx@inu.ac.kr", passwordEncoder.encode("eee"), "까까머리 " + nickname);
            Member member25 = createMember("yyy@inu.ac.kr", passwordEncoder.encode("eee"), "귀여운 " + nickname);
            Member member26 = createMember("zzz@inu.ac.kr", passwordEncoder.encode("eee"), "사랑스런 " + nickname);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);
            em.persist(member5);
            em.persist(member6);
            em.persist(member7);
            em.persist(member8);
            em.persist(member9);
            em.persist(member10);
            em.persist(member11);
            em.persist(member12);
            em.persist(member13);
            em.persist(member14);
            em.persist(member15);
            em.persist(member16);
            em.persist(member17);
            em.persist(member18);
            em.persist(member19);
            em.persist(member20);
            em.persist(member21);
            em.persist(member22);
            em.persist(member23);
            em.persist(member24);
            em.persist(member25);
            em.persist(member26);

            MatchingInfo matchingInfo1 = createMatchingInfo("DORM1",
                    "WEEK16",
                    "FEMALE",
                    23,
                    false,
                    false,
                    false,
                    true,
                    true,
                    "오전 9시에 주로 일어납니다.",
                    "적당히 깨끗한 거 좋아해요..",
                    "주로 외출 전 그리고 밤 11시쯤에 씻어요",
                    "ESTJ",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.23424~",
                    member1);

            MatchingInfo matchingInfo2 = createMatchingInfo("DORM1",
                    "WEEK16",
                    "FEMALE",
                    27,
                    false,
                    false,
                    false,
                    true,
                    true,
                    "오후 3시에 주로 일어납니다.",
                    "깨끗한 거 좋아합니다.",
                    "주로 오후 2-3시 그리고 밤 12시쯤에 씻어요",
                    "ENFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2344~",
                    member2);

            MatchingInfo matchingInfo3 = createMatchingInfo("DORM2",
                    "WEEK16",
                    "FEMALE",
                    30,
                    false,
                    true,
                    false,
                    true,
                    true,
                    "오후 3시에 주로 일어납니다.",
                    "깨끗한 거 좋아합니다.",
                    "주로 오후 2-3시 그리고 밤 12시쯤에 씻어요",
                    "ENFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2344~",
                    member3);

            MatchingInfo matchingInfo4 = createMatchingInfo("DORM1",
                    "WEEK16",
                    "MALE",
                    22,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member4);

            MatchingInfo matchingInfo5 = createMatchingInfo("DORM1",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member5);

            MatchingInfo matchingInfo6 = createMatchingInfo("DORM1",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member6);

            MatchingInfo matchingInfo7 = createMatchingInfo("DORM1",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member7);
            MatchingInfo matchingInfo8 = createMatchingInfo("DORM1",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member8);
            MatchingInfo matchingInfo9 = createMatchingInfo("DORM1",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member9);
            MatchingInfo matchingInfo10 = createMatchingInfo("DORM1",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member10);
            MatchingInfo matchingInfo11 = createMatchingInfo("DORM1",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member11);
            MatchingInfo matchingInfo12 = createMatchingInfo("DORM1",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member12);
            MatchingInfo matchingInfo13 = createMatchingInfo("DORM1",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member13);
            MatchingInfo matchingInfo14 = createMatchingInfo("DORM1",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member14);
            MatchingInfo matchingInfo15 = createMatchingInfo("DORM2",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member15);
            MatchingInfo matchingInfo16 = createMatchingInfo("DORM3",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member16);
            MatchingInfo matchingInfo17 = createMatchingInfo("DORM2",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member17);
            MatchingInfo matchingInfo18 = createMatchingInfo("DORM3",
                    "WEEK24",
                    "MALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member18);
            MatchingInfo matchingInfo19 = createMatchingInfo("DORM2",
                    "WEEK24",
                    "FEMALE",
                    20,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member19);
            MatchingInfo matchingInfo20 = createMatchingInfo("DORM3",
                    "WEEK24",
                    "FEMALE",
                    20,
                    true,
                    true,
                    false,
                    true,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member20);
            MatchingInfo matchingInfo21 = createMatchingInfo("DORM1",
                    "WEEK16",
                    "FEMALE",
                    20,
                    true,
                    false,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member21);
            MatchingInfo matchingInfo22 = createMatchingInfo("DORM1",
                    "WEEK16",
                    "FEMALE",
                    22,
                    false,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member22);
            MatchingInfo matchingInfo23 = createMatchingInfo("DORM1",
                    "WEEK16",
                    "FEMALE",
                    27,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member23);
            MatchingInfo matchingInfo24 = createMatchingInfo("DORM1",
                    "WEEK16",
                    "FEMALE",
                    24,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member24);

            MatchingInfo matchingInfo25 = createMatchingInfo("DORM1",
                    "WEEK16",
                    "FEMALE",
                    29,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member25);

            MatchingInfo matchingInfo26 = createMatchingInfo("DORM1",
                    "WEEK16",
                    "FEMALE",
                    30,
                    true,
                    true,
                    false,
                    false,
                    true,
                    "주로 오전 10시에 일어나요.",
                    "적당히 어지러진 상태로 삽니다.",
                    "잘 안 씻어요",
                    "ESFP",
                    "예민하지 않은 사람이 좋아요",
                    "kakao.2244~",
                    member26);

            em.persist(matchingInfo1);
            em.persist(matchingInfo2);
            em.persist(matchingInfo3);
            em.persist(matchingInfo4);
            em.persist(matchingInfo5);

            em.persist(matchingInfo6);
            em.persist(matchingInfo7);
            em.persist(matchingInfo8);
            em.persist(matchingInfo9);
            em.persist(matchingInfo10);

            em.persist(matchingInfo11);
            em.persist(matchingInfo12);
            em.persist(matchingInfo13);
            em.persist(matchingInfo14);
            em.persist(matchingInfo15);

            em.persist(matchingInfo16);
            em.persist(matchingInfo17);
            em.persist(matchingInfo18);
            em.persist(matchingInfo19);
            em.persist(matchingInfo20);

            em.persist(matchingInfo21);
            em.persist(matchingInfo22);
            em.persist(matchingInfo23);
            em.persist(matchingInfo24);
            em.persist(matchingInfo25);
            em.persist(matchingInfo26);
        }

        public void dbInit3() {
            Email email1 = createEmail("aaa@inu.ac.kr", "111-111");
            Email email2 = createEmail("bbb@inu.ac.kr", "111-111");
            Email email3 = createEmail("ccc@inu.ac.kr", "111-111");
            Email email4 = createEmail("ddd@inu.ac.kr", "111-111");
            Email email5 = createEmail("eee@inu.ac.kr", "111-111");
            Email email6 = createEmail("fff@inu.ac.kr", "111-111");

            Email email7 = createEmail("ggg@inu.ac.kr", "111-111");
            Email email8 = createEmail("hhh@inu.ac.kr", "111-111");
            Email email9 = createEmail("iii@inu.ac.kr", "111-111");
            Email email10 = createEmail("jjj@inu.ac.kr", "111-111");
            Email email11 = createEmail("kkk@inu.ac.kr", "111-111");
            Email email12 = createEmail("lll@inu.ac.kr", "111-111");
            Email email13 = createEmail("mmm@inu.ac.kr", "111-111");
            Email email14 = createEmail("nnn@inu.ac.kr", "111-111");
            Email email15 = createEmail("ooo@inu.ac.kr", "111-111");
            Email email16 = createEmail("ppp@inu.ac.kr", "111-111");
            Email email17 = createEmail("qqq@inu.ac.kr", "111-111");
            Email email18 = createEmail("rrr@inu.ac.kr", "111-111");
            Email email19 = createEmail("sss@inu.ac.kr", "111-111");
            Email email20 = createEmail("ttt@inu.ac.kr", "111-111");
            Email email21 = createEmail("uuu@inu.ac.kr", "111-111");
            Email email22 = createEmail("vvv@inu.ac.kr", "111-111");
            Email email23 = createEmail("www@inu.ac.kr", "111-111");
            Email email24 = createEmail("xxx@inu.ac.kr", "111-111");
            Email email25 = createEmail("yyy@inu.ac.kr", "111-111");
            Email email26 = createEmail("zzz@inu.ac.kr", "111-111");

            em.persist(email1);
            em.persist(email2);
            em.persist(email3);
            em.persist(email4);
            em.persist(email5);
            em.persist(email6);
            em.persist(email7);
            em.persist(email8);
            em.persist(email9);
            em.persist(email10);
            em.persist(email11);
            em.persist(email12);
            em.persist(email13);
            em.persist(email14);
            em.persist(email15);
            em.persist(email16);
            em.persist(email17);
            em.persist(email18);
            em.persist(email19);
            em.persist(email20);
            em.persist(email21);
            em.persist(email22);
            em.persist(email23);
            em.persist(email24);
            em.persist(email25);
            em.persist(email26);
        }

        private Member createMember(String email, String password, String nickname) {
            Member member = new Member(email, password, nickname);
            return member;
        }

        private Email createEmail(String email, String code) {
            Email mail = new Email(email, code);
            mail.isJoined();
            mail.isChecked();
            return mail;
        }

        private MatchingInfo createMatchingInfo(String dormNum,
                                                String joinPeriod,
                                                String gender,
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
                                                String openKakaoLink,
                                                Member member) {

            MatchingInfo matchingInfo = MatchingInfo.builder()
                    .age(age)
                    .cleanUpStatus(cleanUpStatus)
                    .dormNum(dormNum)
                    .gender(gender)
                    .isAllowedFood(isAllowedFood)
                    .isGrinding(isGrinding)
                    .isSmoking(isSmoking)
                    .isSnoring(isSnoring)
                    .isWearEarphones(isWearEarphones)
                    .joinPeriod(joinPeriod)
                    .mbti(mbti)
                    .member(member)
                    .openKakaoLink(openKakaoLink)
                    .showerTime(showerTime)
                    .wakeUpTime(wakeUpTime)
                    .wishText(wishText)
                    .build();

            matchingInfo.updateIsMatchingInfoPublic(true);

            return matchingInfo;
        }
    }
}
