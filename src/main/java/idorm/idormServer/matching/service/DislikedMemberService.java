package idorm.idormServer.matching.service;

import idorm.idormServer.exception.CustomException;
import idorm.idormServer.exception.ExceptionCode;

import idorm.idormServer.matching.domain.DislikedMember;
import idorm.idormServer.matching.repository.DislikedMemberRepository;
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

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;
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
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] DislikedMemberService findDislikedMembers {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * DislikedMember 싫어요한 멤버 조회 |
     */
    public List<DislikedMember> findDislikedMembersByMemberId(Long memberId) {
        log.info("IN PROGRESS | DislikedMember 싫어요한 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);

        try {
            List<DislikedMember> dislikedMembers = dislikedMemberRepository.findAllByMemberId(memberId);
            log.info("COMPLETE | DislikedMember 싫어요한 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);
            return dislikedMembers;
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] DislikedMemberService findDislikedMembersByMemberId {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * DislikedMember 싫어요한 멤버로 등록되어있는지 조회
     * 멤버 식별자와 싫어요한 멤버 식별자를 인자로 받아서 이미 등록되어있다면 true로 반환한다.
     */
    public boolean isRegisteredDislikedMemberIdByMemberId(Long memberId, Long selectedMemberId) {
        log.info("IN PROGRESS | DislikedMember 싫어요한 멤버로 등록 여부 조회 At " + LocalDateTime.now() + " | 멤버 식별자: " +
                memberId + " | 선택한 멤버 식별자 : " + selectedMemberId);

        Optional<Long> registeredDislikedMemberId =
                dislikedMemberRepository.isRegisteredDislikedMemberIdByMemberId(memberId, selectedMemberId);

        if(registeredDislikedMemberId.isPresent()) {
            return true;
        }
        log.info("COMPLETE | DislikedMember 싫어요한 멤버로 등록 여부 조회 At " + LocalDateTime.now() + " | 멤버 식별자: " +
                memberId + " | 선택한 멤버 식별자 : " + selectedMemberId);
        return false;
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
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] DislikedMemberService saveDislikedMember {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
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
                throw new CustomException(ExceptionCode.DUPLICATE_DISLIKED_MEMBER);
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

        memberService.findById(loginMemberId);

        try {
            dislikedMemberRepository.deleteDislikedMember(loginMemberId, dislikedMemberId);
            log.info("COMPLETE | DislikedMember 싫어요한 멤버 삭제 At " + LocalDateTime.now()
                    + " | 로그인 멤버 식별자: " + loginMemberId + " | 싫어요한 멤버 식별자 : " + dislikedMemberId);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] DislikedMemberService deleteDislikedMember {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * DislikedMembers 삭제
     * 회원 탈퇴 시 사용한다. selectedMemberId가 탈퇴한 멤버 식별자일 경우 삭제한다.
     */
    @Transactional
    public void deleteDislikedMembers(Long memberId) {
        log.info("IN PROGRESS | DislikedMember 싫어요한 멤버들 삭제 At " + LocalDateTime.now()
                + " | 로그인 멤버 식별자: " + memberId);

        try {
            dislikedMemberRepository.deleteDislikedMembers(memberId);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] DislikedMemberService deleteDislikedMembers {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }

        log.info("COMPLETE | DislikedMember 싫어요한 멤버들 삭제 At " + LocalDateTime.now()
                + " | 로그인 멤버 식별자: " + memberId);
    }
}
