package idorm.idormServer.matching.service;

import idorm.idormServer.exception.CustomException;

import idorm.idormServer.matching.domain.LikedMember;
import idorm.idormServer.matching.repository.LikedMemberRepository;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikedMemberService {

    private final LikedMemberRepository likedMemberRepository;
    private final MemberService memberService;
    private final MatchingInfoService matchingInfoService;

    /**
     * LikedMember 좋아요한 멤버 조회 |
     */
    public List<LikedMember> findLikedMembersByMemberId(Long memberId) {

        try {
            List<LikedMember> likedMembers = likedMemberRepository.findAllByMemberId(memberId);
            return likedMembers;
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * LikedMember 좋아요한 멤버로 등록되어있는지 조회
     * 멤버 식별자와 선택한 멤버 식별자를 인자로 받아서 이미 좋아요한 멤버로 등록되어있다면 true로 반환한다.
     */
    public boolean isRegisteredlikedMemberIdByMemberId(Long memberId, Long selectedMemberId) {
        Optional<Long> registeredLikedMemberId =
                likedMemberRepository.isRegisteredLikedMemberIdByMemberId(memberId, selectedMemberId);

        if(registeredLikedMemberId.isPresent()) {
            return true;
        }
        return false;
    }


    /**
     * LikedMember 좋아요한 멤버 저장 |
     * 멤버의 좋아요한 멤버를 저장한다. 이미 좋아요한 멤버(중복여부)인지 확인하고 중복 시 409(Conflict)를 던진다.
     * 좋아요한 멤버를 저장 중에 문제가 발생하면 500(Internal Server Error)를 던진다.
     */
    @Transactional
    public Long saveLikedMember(Long memberId, Long likedMemberId) {
        isExistingLikedMember(memberId, likedMemberId);

        Member loginMember = memberService.findById(memberId);
        memberService.findById(likedMemberId);
        matchingInfoService.findByMemberId(likedMemberId);

        try {
            LikedMember likedMember = LikedMember.builder()
                    .loginMember(loginMember)
                    .likedMemberId(likedMemberId)
                    .build();

            likedMemberRepository.save(likedMember);
            return likedMember.getId();
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 중복 좋아요한 멤버 여부 확인 |
     * 좋아요한 멤버의 중복 여부를 확인하고, 이미 좋아요한 멤버라면 409(Conflict)를 던진다.
     * 만약 조회 중에 에러가 발생하면 500(Internal Server Error)를 던진다.
     */
    private void isExistingLikedMember(Long memberId, Long likedMemberId) {

        List<Long> likedMemberIds = likedMemberRepository.findLikedMembersByMemberId(memberId);

        for(Long id : likedMemberIds) {
            if(id == likedMemberId) {
                throw new CustomException(DUPLICATE_LIKED_MEMBER);
            }
        }
    }

    /**
     * 좋아요한 멤버 삭제
     */
    @Transactional
    public void deleteLikedMember(Long loginMemberId, Long likedMemberId) {
        memberService.findById(loginMemberId);

        try {
            likedMemberRepository.deleteLikedMember(loginMemberId, likedMemberId);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * LikedMembers 삭제
     * 회원 탈퇴 시 사용한다. selectedMemberId가 탈퇴한 멤버 식별자일 경우 삭제한다.
     */
    @Transactional
    public void deleteLikedMembers(Long memberId) {
        try {
            likedMemberRepository.deleteLikedMembers(memberId);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }
}
