package idorm.idormServer.service;

import idorm.idormServer.domain.*;
import idorm.idormServer.repository.MatchingInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingInfoService {

    private final MatchingInfoRepository matchingInfoRepository;

    /**
     * 온보딩(매창)정보 생성
     */
    @Transactional
    public Long save(Dormitory dormNum, JoinPeriod joinPeriod, Gender gender, Integer age,
                     Boolean isSnoring, Boolean isSmoking, Boolean isGrinding, Boolean isWearEarphones,
                     Boolean isAllowedFood, String wakeUpTime, String cleanUpStatus, String showerTime,
                     String mbti, String wishText, String openKakaoLink) {

        MatchingInfo matchingInfo = new MatchingInfo(dormNum, joinPeriod, gender, age, isSnoring, isSmoking, isGrinding, isWearEarphones, isAllowedFood, wakeUpTime, cleanUpStatus,
                showerTime, mbti, wishText, openKakaoLink);

        matchingInfoRepository.save(matchingInfo);

        return matchingInfo.getId();
    }

    /**
     * 온보딩(매칭)정보 조회
     */
    public MatchingInfo findById(Long matchingInfoId) {

        return matchingInfoRepository.findById(matchingInfoId).orElseThrow(() -> new NullPointerException(("id가 존재하지 않습니다.")));
    }

    public List<MatchingInfo> findAll() {
        return matchingInfoRepository.findAll();
    }

    /**
     * 온보딩(매칭)정보 삭제
     */
    public void deleteMatchingInfo(Long matchingInfoId) {
        matchingInfoRepository.delete(findById(matchingInfoId));
    }

    /**
     * 온보딩(매칭)정보 수정
     */
    @Transactional
    public void updateMatchingInfo(Long matchingInfoId, Dormitory dormNum, JoinPeriod joinPeriod, Gender gender, Integer age,
                                   Boolean isSnoring, Boolean isSmoking, Boolean isGrinding, Boolean isWearEarphones,
                                   Boolean isAllowedFood, String wakeUpTime, String cleanUpStatus, String showerTime,
                                   String mbti, String wishText, String openKakaoLink) {

        MatchingInfo matchingInfo = matchingInfoRepository.findById(matchingInfoId).get();

        if(dormNum != null) matchingInfo.updateDormNum(dormNum);


//        matchingInfo.updateAllMatchingInfo(dormNum, joinPeriod, gender, age, isSnoring, isSmoking, isGrinding, isWearEarphones,
//                isAllowedFood, wakeUpTime, cleanUpStatus, showerTime, mbti, wishText, openKakaoLink);

    }



}
