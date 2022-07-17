package idorm.idormServer.service;

import idorm.idormServer.domain.MyInfo;
import idorm.idormServer.dto.MyInfoDTO.MyInfoOneDTO;
import idorm.idormServer.repository.MyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyInfoService {

    private final MyInfoRepository myInfoRepository;

    /**
     * 회원 정보 저장
     */
    @Transactional(readOnly = false)
    public Long save(MyInfo myInfo) {
        // 의문점: 저장할 때도 엔티티 자체를 넘기지 않고, dto로 변환 혹은 파라미터로 받아야할까

        myInfoRepository.saveMyInfo(myInfo);
        return myInfo.getId();
    }

    /**
     * 회원 정보 조회
     */
    public MyInfoOneDTO findMyInfoById(Long myInfoId) {
        return myInfoRepository.findOne(myInfoId);
    }

    public List<MyInfo> findMyInfoAll() {
        return myInfoRepository.findAll();
    }

//    @Transactional
//    public void deleteMyInfo(Long myInfoId) {
//        myInfoRepository.delete(myInfoId);
//    }

}
