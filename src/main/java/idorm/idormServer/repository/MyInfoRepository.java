package idorm.idormServer.repository;

import idorm.idormServer.domain.MyInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
}
