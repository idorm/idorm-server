package idorm.idormServer.matchingInfo.service;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.dto.MatchingInfoSaveRequestDto;
import idorm.idormServer.matchingInfo.dto.MatchingInfoUpdateRequestDto;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import idorm.idormServer.matchingInfo.repository.MatchingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public Optional<MatchingInfo> findByMemberIdOp(Long memberId) {
        Optional<MatchingInfo> matchingInfo = Optional.ofNullable(memberService.findById(memberId).getMatchingInfo());
        return matchingInfo;
    }

    /**
     * 온보딩(매칭)정보 삭제
     */
    public void deleteMatchingInfo(Member member, MatchingInfo matchingInfo) {

        matchingInfoRepository.delete(matchingInfo);
        matchingInfoRepository.save(matchingInfo);
        memberService.deleteMatchingInfo(member);
    }

    /**
     * 온보딩(매칭)정보 수정
     */
    @Transactional
    public void updateMatchingInfo(MatchingInfo matchingInfo, MatchingInfoUpdateRequestDto request) {

        matchingInfo.updateDormNum(request.getDormNum());
        matchingInfo.updateJoinPeriod(request.getJoinPeriod());
        matchingInfo.updateGender(request.getGender());
        matchingInfo.updateAge(request.getAge());
        matchingInfo.updateIsSnoring(request.getIsSnoring());
        matchingInfo.updateIsSmoking(request.getIsSmoking());
        matchingInfo.updateIsGrinding(request.getIsGrinding());
        matchingInfo.updateIsWearEarphones(request.getIsWearEarphones());
        matchingInfo.updateIsAllowedFood(request.getIsAllowedFood());
        matchingInfo.updateWakeupTime(request.getWakeUpTime());
        matchingInfo.updateCleanUpStatus(request.getCleanUpStatus());
        matchingInfo.updateShowerTime(request.getShowerTime());
        matchingInfo.updateMbti(request.getMbti());
        matchingInfo.updateWishtext(request.getWishText());
        matchingInfo.updateOpenKakaoLink(request.getOpenKakaoLink());

        matchingInfoRepository.save(matchingInfo);
    }

}
