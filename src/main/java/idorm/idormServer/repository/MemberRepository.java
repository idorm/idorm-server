package idorm.idormServer.repository;

import idorm.idormServer.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    /**
     * 회원 가입
     */
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    /**
     * 회원 조회
     */
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

//    public List<Member> findById(Long id) {
//
//        return em.createQuery("select m from Member m where m.id = :id",
//                Member.class)
//                .setParameter("id", id)
//                .getResultList();
//    }

    public List<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email=:email",
                Member.class)
                .setParameter("email", email)
                .getResultList();
    }

}
