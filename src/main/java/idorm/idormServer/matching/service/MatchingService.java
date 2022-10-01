package idorm.idormServer.matching.service;

import idorm.idormServer.exceptions.http.ConflictException;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.matching.dto.MatchingFilteredMatchingInfoRequestDto;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.repository.MatchingInfoRepository;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingService {

    private final MemberRepository memberRepository;

    private final MatchingInfoRepository matchingInfoRepository;
    private final MemberService memberService;
    private final MatchingInfoService matchingInfoService;

    /**
     * Matching 매칭멤버 전체 조회 |
     * MatchingInfo를 통해 조건에 맞는 멤버들로 필터링 후, 싫어요한 멤버를 필터링합니다.
     * 필터링된 MatchingInfo 식별자를 반환합니다.
     */
    public List<Long> findMatchingMembers(Long memberId) {

        log.info("IN PROGRESS | Matching 매칭멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);

        Member loginMember = memberService.findById(memberId);

        if(loginMember.getMatchingInfo() == null) {
            throw new ConflictException("매칭정보가 존재하지 않습니다.");
        }

        MatchingInfo loginMemberMatchingInfo = loginMember.getMatchingInfo();

        try {
             List<Long> filteredMatchingInfoId = matchingInfoRepository.findMatchingMembers(
                    memberId,
                    loginMemberMatchingInfo.getDormNum(),
                    loginMemberMatchingInfo.getJoinPeriod(),
                    loginMemberMatchingInfo.getGender()
            );

             // TODO: 싫어요한 멤버 필터링

            return filteredMatchingInfoId;
        } catch (Exception e) {
            throw new InternalServerErrorException("Matching 매칭멤버 조회 중 서버 에러 발생", e);
        }
    }

    /**
     * Matching 매칭멤버 필터링 조회 |
     */
    public List<Long> findFilteredMatchingMembers(Long memberId,
                                                  MatchingFilteredMatchingInfoRequestDto filteringRequest) {
        log.info("IN PROGRESS | Matching 필터링된 매칭멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);

        Member loginMember = memberService.findById(memberId);

        if(loginMember.getMatchingInfo() == null) {
            throw new ConflictException("매칭정보가 존재하지 않습니다.");
        }

        // TODO: true일 경우 false,true 모두 괜찮은 경우이므로 둘 다 조회되야함
        int isSnoring = (filteringRequest.getIsSnoring() == true) ? 1 : 0; // true:1 , false: 0
        int isSmoking = (filteringRequest.getIsSmoking() == true) ? 1 : 0;
        int isGrinding = (filteringRequest.getIsGrinding() == true) ? 1 : 0;
        int isWearEarphones = (filteringRequest.getIsWearEarphones() == true) ? 1 : 0;
        int isAllowedFood = (filteringRequest.getIsAllowedFood() == true) ? 1 : 0;

        try {
            List<Long> filteredMatchingInfoId = matchingInfoRepository.findFilteredMatchingMembers(
                    memberId,
                    filteringRequest.getDormNum(),
                    filteringRequest.getJoinPeriod(),
                    filteringRequest.getGender(),
                    isSnoring,
                    isSmoking,
                    isGrinding,
                    isWearEarphones,
                    isAllowedFood,
                    filteringRequest.getMinAge(),
                    filteringRequest.getMaxAge()
            );

            // TODO: 싫어요한 멤버 필터링
            log.info("COMPLETE | Matching 필터링된 매칭멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);
            return filteredMatchingInfoId;
        } catch (Exception e) {
            throw new InternalServerErrorException("Matching 매칭멤버 조회 중 서버 에러 발생", e);
        }
    }


    /**
     * Matching 좋아요한 매칭멤버 조회 |
     * 추가로직: 매칭 이미지 여부가 false일 경우 조회되지 않게 처리
     */
//    public List<MatchingInfo> findMatchingLikedMembers(Long memberId) {
//        log.info("IN PROGRESS | Matching 좋아요한 매칭멤버 조회 At " + LocalDateTime.now() + " | " + memberId);
//
//        Member loginMember = memberService.findById(memberId);
//
//        if(loginMember.getMatchingInfo() == null) {
//            throw new ConflictException("매칭정보가 존재하지 않습니다.");
//        }
//
//        log.info(loginMember.getLikedMembers().toString());
//        try {
//            List<Member> likedMembers = memberService.findMatchingLikedMembers(memberId);
//            List<MatchingInfo> likedMatchingInfoList = new ArrayList<>();
//
//            for(Member likedMember : likedMembers) {
//
//                Long matchingInfoId = matchingInfoService.findByMemberId(likedMember.getId());
//
//                MatchingInfo matchingInfo = matchingInfoService.findById(matchingInfoId);
//                likedMatchingInfoList.add(matchingInfo);
//            }
//            log.info("COMPLETE | Matching 좋아요한 매칭멤버 조회 At " + LocalDateTime.now() + " | " + memberId);
//            return likedMatchingInfoList;
//        } catch (Exception e) {
//            throw new InternalServerErrorException("Matching 좋아요한 매칭멤버 조회 중 서버 에러 발생", e);
//        }
//    }

    /**
     * Matching 좋아요한 매칭멤버 추가 |
     */
//    public void addMatchingLikedMember(Long memberId, Long likedMemberId) {
//
//        log.info("IN PROGRESS | Matching 좋아요한 매칭멤버 추가 At " + LocalDateTime.now() + " | 로그인 멤버 식별자: " + memberId);
//
//        Member loginMember = memberService.findById(memberId);
//        Member likedMember = memberService.findById(likedMemberId);
//        matchingInfoService.findByMemberId(likedMemberId);
//
//        try {
//            loginMember.addLikedMember(likedMember);
//            memberRepository.save(loginMember);
//            log.info("COMPLETE | Matching 좋아요한 매칭멤버 추가 At " + LocalDateTime.now() + " | 로그인 멤버 식별자 " + memberId);
//        } catch (Exception e) {
//            throw new InternalServerErrorException("Matching 좋아요한 매칭멤버 추가 중 서버 에러 발생", e);
//        }
//    }

    /**
     * Matching 좋아요한 매칭멤버 삭제 |
     */
//    public void deleteMatchingLikedMember(Long memberId, Long likedMemberId) {
//
//        log.info("IN PROGRESS | Matching 좋아요한 매칭멤버 삭제 At " + LocalDateTime.now() + " | " + memberId);
//
//        Member loginMember = memberService.findById(memberId);
//
//        if(loginMember.getMatchingInfo() == null) {
//            throw new ConflictException("매칭정보가 존재하지 않습니다.");
//        }
//
//        try {
//            memberService.deleteMatchingLikedMember(memberId, likedMemberId);
//            log.info("COMPLETE | Matching 좋아요한 매칭멤버 삭제 At " + LocalDateTime.now() + " | " + memberId);
//        } catch (Exception e) {
//            throw new InternalServerErrorException("Matching 좋아요한 매칭멤버 삭제 중 서버 에러 발생", e);
//        }
//    }

    /**
     * Matching 싫어요한 매칭멤버 추가 |
     */

    /**
     * Matching 싫어요한 매칭멤버 삭제 |
     */

}
