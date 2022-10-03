package idorm.idormServer.matching.service;

import idorm.idormServer.exceptions.http.ConflictException;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.matching.domain.DislikedMember;
import idorm.idormServer.matching.repository.DislikedMemberRepository;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static idorm.idormServer.matching.domain.DislikedMember.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DislikedMemberService {

    private final DislikedMemberRepository dislikedMemberRepository;
    private final MemberService memberService;
    private final MatchingInfoService matchingInfoService;

    /**
     * DislikedMember 싫어요한 멤버 조회 |
     * Member 식별자에 의해 조회되는 싫어요한 멤버의 전체 식별자를 조회합니다.
     */
    public List<Long> findDislikedMembers(Long memberId) {
        log.info("IN PROGRESS | DislikedMember 싫어요 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);

        try {
            List<Long> DislikedMembers = dislikedMemberRepository.findDislikedMembersByMemberId(memberId);
            log.info("COMPLETE | LikedMember 싫어요한 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);
            return DislikedMembers;
        } catch (Exception e) {
            throw new InternalServerErrorException("DislikedMembers 싫어요한 멤버 전체 조회 중 서버 에러 발생", e);
        }
    }

    /**
     * DislikedMember 싫어요한 멤버 저장 |
     * 멤버의 싫어요한 멤버를 저장한다. 이미 싫어요한 멤버(중복여부)인지 확인하고 중복 시 409(Conflict)를 던진다.
     * 싫어요한 멤버를 저장 중에 문제가 발생하면 500(Internal Server Error)를 던진다.
     */
    @Transactional
    public Long saveDislikedMember(Long memberId, Long DislikedMemberId) {
        log.info("IN PROGRESS | DislikedMember 싫어요한 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);

        isExistingDislikedMember(memberId, DislikedMemberId);

        Member loginMember = memberService.findById(memberId);
        memberService.findById(DislikedMemberId);
        matchingInfoService.findByMemberId(DislikedMemberId);

        try {
            DislikedMember DislikedMember = builder()
                    .loginMember(loginMember)
                    .dislikedMemberId(DislikedMemberId)
                    .build();

            dislikedMemberRepository.save(DislikedMember);
            log.info("COMPLETE | DislikedMember 싫어요한 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);
            return DislikedMember.getId();
        } catch (Exception e) {
            throw new InternalServerErrorException("DislikedMember save 중 서버 에러 발생", e);
        }
    }

    /**
     * 중복 싫어요한 멤버 여부 확인 |
     * 싫어요한 멤버의 중복 여부를 확인하고, 이미 싫어요한 멤버라면 409(Conflict)를 던진다.
     * 만약 조회 중에 에러가 발생하면 500(Internal Server Error)를 던진다.
     */
    private void isExistingDislikedMember(Long memberId, Long dislikedMemberId) {

        log.info("IN PROGRESS | DislikedMember 중복 여부 확인 At " + LocalDateTime.now() + " | " + memberId);

        List<Long> dislikedMemberIds = dislikedMemberRepository.findDislikedMembersByMemberId(memberId);

        for(Long id : dislikedMemberIds) {
            if(id == dislikedMemberId) {
                throw new ConflictException("이미 싫어요한 멤버입니다.");
            }
        }

        log.info("COMPLETE | DislikedMember 중복 여부 확인 At " + LocalDateTime.now() + " | " + memberId);
    }

    /**
     * 싫어요한 멤버 삭제
     */
    @Transactional
    public void deleteDislikedMember(Long loginMemberId, Long dislikedMemberId) {

        log.info("IN PROGRESS | DislikedMember 싫어요한 멤버 삭제 At " + LocalDateTime.now()
                + " | 로그인 멤버 식별자: " + loginMemberId + " | 싫어요한 멤버 식별자 : " + dislikedMemberId);

        // 삭제할 싫어요한 멤버가 존재하는지 확인하기 위한 코드
        memberService.findById(loginMemberId);
        List<Long> dislikedMemberIds = dislikedMemberRepository.findDislikedMembersByMemberId(loginMemberId);

        boolean isExistDislikedMember = false;

        for(Long id : dislikedMemberIds) {
            if(id == dislikedMemberId) {
                isExistDislikedMember = true;
            }
        }

        if(isExistDislikedMember == false) {
            throw new ConflictException("해당 아이디의 싫어요한 멤버는 존재하지 않기 때문에 삭제할 수 없습니다.");
        }

        try {
            dislikedMemberRepository.deleteDislikedMember(loginMemberId, dislikedMemberId);
            log.info("COMPLETE | DislikedMember 싫어요한 멤버 삭제 At " + LocalDateTime.now()
                    + " | 로그인 멤버 식별자: " + loginMemberId + " | 싫어요한 멤버 식별자 : " + dislikedMemberId);
        } catch (Exception e) {
            throw new InternalServerErrorException("DislikedMember 싫어요한 멤버 삭제 중 서버 에러 발생", e);
        }
    }
}
