package idorm.idormServer.matching.service;

import idorm.idormServer.exception.CustomException;

import idorm.idormServer.matching.domain.LikedMember;
import idorm.idormServer.matching.repository.LikedMemberRepository;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static idorm.idormServer.exception.ExceptionCode.*;

@Slf4j
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
        log.info("IN PROGRESS | LikedMember 좋아요한 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);

        try {
            List<LikedMember> likedMembers = likedMemberRepository.findAllByMemberId(memberId);
            log.info("COMPLETE | LikedMember 좋아요한 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);
            return likedMembers;
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] LikedMemberService findLikedMembersByMemberId {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * LikedMember 좋아요한 멤버로 등록되어있는지 조회
     * 멤버 식별자와 선택한 멤버 식별자를 인자로 받아서 이미 좋아요한 멤버로 등록되어있다면 true로 반환한다.
     */
    public boolean isRegisteredlikedMemberIdByMemberId(Long memberId, Long selectedMemberId) {
        log.info("IN PROGRESS | LikedMember 좋아요한 멤버로 등록 여부 조회 At " + LocalDateTime.now() + " | 멤버 식별자: " +
                memberId + " | 선택한 멤버 식별자 : " + selectedMemberId);

        Optional<Long> registeredLikedMemberId =
                likedMemberRepository.isRegisteredLikedMemberIdByMemberId(memberId, selectedMemberId);

        if(registeredLikedMemberId.isPresent()) {
            return true;
        }
        log.info("COMPLETE | LikedMember 좋아요한 멤버로 등록 여부 조회 At " + LocalDateTime.now() + " | 멤버 식별자: " +
                memberId + " | 선택한 멤버 식별자 : " + selectedMemberId);
        return false;
    }


    /**
     * LikedMember 좋아요한 멤버 저장 |
     * 멤버의 좋아요한 멤버를 저장한다. 이미 좋아요한 멤버(중복여부)인지 확인하고 중복 시 409(Conflict)를 던진다.
     * 좋아요한 멤버를 저장 중에 문제가 발생하면 500(Internal Server Error)를 던진다.
     */
    @Transactional
    public Long saveLikedMember(Long memberId, Long likedMemberId) {
        log.info("IN PROGRESS | LikedMember 좋아요한 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);

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
            log.info("COMPLETE | LikedMember 좋아요한 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);
            return likedMember.getId();
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] LikedMemberService saveLikedMember {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 중복 좋아요한 멤버 여부 확인 |
     * 좋아요한 멤버의 중복 여부를 확인하고, 이미 좋아요한 멤버라면 409(Conflict)를 던진다.
     * 만약 조회 중에 에러가 발생하면 500(Internal Server Error)를 던진다.
     */
    private void isExistingLikedMember(Long memberId, Long likedMemberId) {

        log.info("IN PROGRESS | LikedMember 중복 여부 확인 At " + LocalDateTime.now() + " | " + memberId);

        List<Long> likedMemberIds = likedMemberRepository.findLikedMembersByMemberId(memberId);

        for(Long id : likedMemberIds) {
            if(id == likedMemberId) {
                throw new CustomException(DUPLICATE_LIKED_MEMBER);
            }
        }

        log.info("COMPLETE | LikedMember 중복 여부 확인 At " + LocalDateTime.now() + " | " + memberId);
    }

    /**
     * 좋아요한 멤버 삭제
     */
    @Transactional
    public void deleteLikedMember(Long loginMemberId, Long likedMemberId) {

        log.info("IN PROGRESS | LikedMember 좋아요한 멤버 삭제 At " + LocalDateTime.now()
                + " | 로그인 멤버 식별자: " + loginMemberId + " | 좋아요한 멤버 식별자 : " + likedMemberId);

        memberService.findById(loginMemberId);

        try {
            likedMemberRepository.deleteLikedMember(loginMemberId, likedMemberId);
            log.info("COMPLETE | LikedMember 좋아요한 멤버 삭제 At " + LocalDateTime.now()
                    + " | 로그인 멤버 식별자: " + loginMemberId + " | 좋아요한 멤버 식별자 : " + likedMemberId);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] LikedMemberService deleteLikedMember {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * LikedMembers 삭제
     * 회원 탈퇴 시 사용한다. selectedMemberId가 탈퇴한 멤버 식별자일 경우 삭제한다.
     */
    @Transactional
    public void deleteLikedMembers(Long memberId) {
        log.info("IN PROGRESS | LikedMember 좋아요한 멤버들 삭제 At " + LocalDateTime.now()
                + " | 로그인 멤버 식별자: " + memberId);

        try {
            likedMemberRepository.deleteLikedMembers(memberId);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] LikedMemberService deleteLikedMembers {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }

        log.info("COMPLETE | LikedMember 좋아요한 멤버들 삭제 At " + LocalDateTime.now()
                + " | 로그인 멤버 식별자: " + memberId);
    }
}
