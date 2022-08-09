//package idorm.idormServer.service;
//
//import idorm.idormServer.domain.*;
//import idorm.idormServer.repository.MatchingInfoRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class MatchingInfoService {
//
//    private final MatchingInfoRepository matchingInfoRepository;
//
//    /**
//     * 회원 정보 저장
//     */
//    @Transactional
//    public Long save(Dormitory dormNum, JoinPeriod joinPeriod, Gender gender,
//                     Integer age, Boolean isSnoring, Boolean isSmoking,
//                     Boolean isGrinding, Boolean isWearEarphones, Boolean isAllowedFood,
//                     String wakeUpTime, String cleanUpStatus, String showerTime,
//                     String MBTI, String wishText) {
//        MatchingInfo myInfo = new MatchingInfo(dormNum, joinPeriod, gender,
//                age, isSnoring, isSmoking,
//                isGrinding, isWearEarphones, isAllowedFood,
//                wakeUpTime, cleanUpStatus, showerTime,
//                MBTI, wishText);
//
//        return myInfo.getId();
//    }
//
//    /**
//     * 회원 정보 조회
//     */
//    public MatchingInfo findMyInfoById(Long myInfoId) {
//        return matchingInfoRepository.findById(myInfoId).get();
//    }
//
//    public List<MatchingInfo> findMyInfoAll() {
//        return matchingInfoRepository.findAll();
//    }
//
//    public List<MatchingInfo> findAll() {
//        return matchingInfoRepository.findAll();
//    }
//
//    @Transactional
//    public void deleteMyInfo(Long myInfoId) {
//        matchingInfoRepository.delete(findMyInfoById(myInfoId));
//    }
//}
