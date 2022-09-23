package idorm.idormServer.matching.service;

import idorm.idormServer.exceptions.http.ConflictException;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * Member 매칭멤버 조회 |
     * MatchingInfo를 통해 조건에 맞는 멤버들로 필터링 후, 싫어요한 멤버를 필터링합니다.
     */
    public List<Member> findMatchingMembers(Long memberId) {

        Member loginMember = memberService.findById(memberId);

        if(loginMember.getMatchingInfo() == null) {
            throw new ConflictException("매칭정보가 존재하지 않습니다.");
        }

        MatchingInfo loginMemberMatchingInfo = loginMember.getMatchingInfo();

        /**
         * 조건:
         * 1. isMatchingInfoPublic: true 매칭이미지 공개여부가 true
         * 2. dormNum 기숙사 선택 같아야함
         * 3. joinPeriod 입사기간 같아야함
         * 4. gender 성별 같아야함
         */
        try {
             List<Member> filteredMembers = memberRepository.findMatchingMembers(
                    memberId,
                    loginMemberMatchingInfo.getDormNum(),
                    loginMemberMatchingInfo.getJoinPeriod(),
                    loginMemberMatchingInfo.getGender()
            );

            return filteredMembers;
        } catch (Exception e) {
            throw new InternalServerErrorException("Member 매칭멤버 조회 중 서버 에러 발생", e);
        }
    }

    /**
     * Member 좋아요한 매칭멤버 조회 |
     */



    /**
     * Member 좋아요한 매칭멤버 추가 |
     */

    /**
     * Member 좋아요한 매칭멤버 삭제 |
     */

}
