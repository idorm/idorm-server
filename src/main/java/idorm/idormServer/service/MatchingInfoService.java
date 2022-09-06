package idorm.idormServer.service;

import idorm.idormServer.domain.*;
import idorm.idormServer.dto.MatchingInfoSaveRequestDto;
import idorm.idormServer.repository.MatchingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingInfoService {

    private final MatchingInfoRepository matchingInfoRepository;
    private final MemberService memberService;


    /**
     * 온보딩(매창)정보 생성
     */
    @Transactional
    public Long save(MatchingInfoSaveRequestDto request, Member member) {
        log.info("START | MatchingInfo Service 저장 At " + LocalDateTime.now());
        try {
            MatchingInfo matchingInfo = request.toEntity(member);
            matchingInfoRepository.save(matchingInfo);
            memberService.updateMatchingInfo(member, matchingInfo);
            log.info("COMPLETE | MatchingInfo 저장 At " + LocalDateTime.now() + " | email: " + matchingInfo.getMember().getEmail());
            return matchingInfo.getId();
        } catch(Exception e) {
            throw new IllegalStateException("MatchingInfo 등록 실패 | err message: " + e);
        }
    }

    /**
     * 온보딩(매칭)정보 조회
     */
    public MatchingInfo findById(Long matchingInfoId) {

        return matchingInfoRepository.findById(matchingInfoId).orElseThrow(() -> new NullPointerException(("MatchingInfo id가 존재하지 않습니다.")));
    }

    public List<MatchingInfo> findAll() {
        return matchingInfoRepository.findAll();
    }

    public MatchingInfo findByMemberIdOp(Long memberId) {
        MatchingInfo matchingInfo = memberService.findById(memberId).getMatchingInfo();
        return matchingInfo;
    }

    /**
     * 온보딩(매칭)정보 삭제
     */
    public void deleteMatchingInfo(Long matchingInfoId) {

        MatchingInfo matchingInfo = matchingInfoRepository.findById(matchingInfoId).get();
        matchingInfo.updateIsVisible();
    }

    /**
     * 온보딩(매칭)정보 수정
     */
//    @Transactional
//    public void updateMatchingInfo(Long matchingInfoId, MatchingInfoUpdateRequestDTO MatchingInfoUpdateRequestDTO) {
//
//        MatchingInfo matchingInfo = matchingInfoRepository.findById(matchingInfoId).get();
//
//        MatchingInfo matchingInfoDto = MatchingInfoUpdateRequestDTO.toEntity();
//
//        matchingInfo.updateDormNum(matchingInfoDto.getDormNum());
//        matchingInfo.updateJoinPeriod(matchingInfoDto.getJoinPeriod());
//        matchingInfo.updateGender(matchingInfoDto.getGender());
//        matchingInfo.updateAge(matchingInfoDto.getAge());
//        matchingInfo.updateIsSnoring(matchingInfoDto.getIsSnoring());
//        matchingInfo.updateIsSmoking(matchingInfoDto.getIsSmoking());
//        matchingInfo.updateIsGrinding(matchingInfoDto.getIsGrinding());
//        matchingInfo.updateIsWearEarphones(matchingInfoDto.getIsWearEarphones());
//        matchingInfo.updateIsAllowedFood(matchingInfoDto.getIsAllowedFood());
//        matchingInfo.updateWakeupTime(matchingInfoDto.getWakeUpTime());
//        matchingInfo.updateCleanUpStatus(matchingInfoDto.getCleanUpStatus());
//        matchingInfo.updateShowerTime(matchingInfoDto.getShowerTime());
//        matchingInfo.updateMbti(matchingInfoDto.getMbti());
//        matchingInfo.updateWishtext(matchingInfoDto.getWishText());
//        matchingInfo.updateOpenKakaoLink(matchingInfoDto.getOpenKakaoLink());
//
//        matchingInfoRepository.save(matchingInfo);
//    }

}
