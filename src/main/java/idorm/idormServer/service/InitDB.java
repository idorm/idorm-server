package idorm.idormServer.service;

import idorm.idormServer.domain.Email;
import idorm.idormServer.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:login.properties")
public class InitDB {

    private final InitService initService;

    @PostConstruct // 스프링빈이 다 올라오면 스프링이 호출
    public void init() {
        // 여기에 InitService를 넣어줘도 되지만 굳이 transaction을 따로 먹여서 호출하는 이유는
        // spring life cycle이 있어서 transaction 등이 잘 작동이 안됨. 그래서 별도의 bean으로 등록하고 호출하는 방식으로 사용하는 것을 권장
         initService.dbInit1();
         initService.dbInit2();
        initService.dbInit3();
        initService.dbInit4();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Value("${id}")
        private String id;

        @Value("${password}")
        private String password;

        public void dbInit1() {
            Member admin = createMember(id, passwordEncoder.encode(password));
            em.persist(admin);

            admin.getRoles().clear();

            admin.getRoles().add("ROLE_ADMIN");
            admin.updateNickname("root");

        }

        public void dbInit2() {
            Member member1 = createMember("aaa@inu.ac.kr", passwordEncoder.encode("aaa"));
            Member member2 = createMember("bbb@inu.ac.kr", passwordEncoder.encode("bbb"));
            Member member3 = createMember("ccc@inu.ac.kr", passwordEncoder.encode("ccc"));

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
        }

        public void dbInit3() {
            Email email1 = createEmail("aaa@inu.ac.kr", "111-111");
            Email email2 = createEmail("bbb@inu.ac.kr", "222-222");
            Email email3 = createEmail("ccc@inu.ac.kr", "333-333");

            em.persist(email1);
            em.persist(email2);
            em.persist(email3);
        }

        public void dbInit4() {
            Email email = new Email("knh709@inu.ac.kr", "777-777");
            email.isChecked();
            em.persist(email);
        }

        private Member createMember(String email, String password) {
            Member member = new Member(email, password);
            return member;
        }

        private Email createEmail(String email, String code) {
            Email mail = new Email(email, code);
            mail.isJoined();
            mail.isChecked();
            return mail;
        }

    }
}
