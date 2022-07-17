package idorm.idormServer.repository;

import idorm.idormServer.domain.MyInfo;
import idorm.idormServer.dto.MyInfoDTO;
import idorm.idormServer.dto.MyInfoDTO.MyInfoOneDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyInfoRepository {

    private final EntityManager em;
    /**
     * 회원 정보 저장
     */
    public Long saveMyInfo(MyInfo myInfo) {
        em.persist(myInfo);
        return myInfo.getId();
    }

    /**
     * 회원 정보 조회
     */
    public MyInfoOneDTO findOne(Long id) {
        MyInfo myInfo = em.find(MyInfo.class, id);
        return new MyInfoOneDTO(myInfo);
    }

    public List<MyInfo> findAll() {
        return em.createQuery("select m from MyInfo m", MyInfo.class)
                .getResultList();
    }

    public List<MyInfo> findByEmail(String email) {
        return em.createQuery(
                "select i from MyInfo i" +
                        " join fetch i.member m" +
                        " where m.email=:email", MyInfo.class
        ).getResultList();

    }
}
