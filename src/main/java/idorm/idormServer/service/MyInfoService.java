package idorm.idormServer.service;

import idorm.idormServer.domain.*;
import idorm.idormServer.dto.MyInfoDTO;
import idorm.idormServer.repository.MyInfoRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyInfoService {

    private final MyInfoRepository myInfoRepository;

    /**
     * 회원 정보 저장
     */
    @Transactional
    public Long save(Dormitory dormNum, JoinPeriod joinPeriod, Gender gender,
                     Integer age, Boolean isSnoring, Boolean isSmoking,
                     Boolean isGrinding, Boolean isWearEarphones, Boolean isAllowedFood,
                     String wakeUpTime, String cleanUpStatus, String showerTime,
                     String MBTI, String wishText) {
        MyInfo myInfo = new MyInfo(dormNum, joinPeriod, gender,
                age, isSnoring, isSmoking,
                isGrinding, isWearEarphones, isAllowedFood,
                wakeUpTime, cleanUpStatus, showerTime,
                MBTI, wishText);

        return myInfo.getId();
    }

    /**
     * 회원 정보 조회
     */
    public MyInfo findMyInfoById(Long myInfoId) {
        return myInfoRepository.findById(myInfoId).get();
    }

    public List<MyInfo> findMyInfoAll() {
        return myInfoRepository.findAll();
    }

    public List<MyInfo> findAll() {
        return myInfoRepository.findAll();
    }

    @Transactional
    public void deleteMyInfo(Long myInfoId) {
        myInfoRepository.delete(findMyInfoById(myInfoId));
    }
}
