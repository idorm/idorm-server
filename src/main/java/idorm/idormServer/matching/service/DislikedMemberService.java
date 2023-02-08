//package idorm.idormServer.matching.service;
//
//import idorm.idormServer.exception.CustomException;
//import idorm.idormServer.exception.ExceptionCode;
//
//import idorm.idormServer.matching.domain.DislikedMember;
//import idorm.idormServer.matching.repository.DislikedMemberRepository;
//import idorm.idormServer.matchingInfo.service.MatchingInfoService;
//import idorm.idormServer.member.domain.Member;
//import idorm.idormServer.member.service.MemberService;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//import static idorm.idormServer.exception.ExceptionCode.*;
//import static idorm.idormServer.matching.domain.DislikedMember.*;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class DislikedMemberService {
//
//    private final DislikedMemberRepository dislikedMemberRepository;
//    private final MemberService memberService;
//    private final MatchingInfoService matchingInfoService;
//
//    /**
//     * DislikedMember 싫어요한 멤버 조회 |
//     * Member 식별자에 의해 조회되는 싫어요한 멤버의 전체 식별자를 조회합니다.
//     */
//    public List<Long> findDislikedMembers(Long memberId) {
//
//        try {
//            List<Long> DislikedMembers = dislikedMemberRepository.findDislikedMembersByMemberId(memberId);
//            return DislikedMembers;
//        } catch (RuntimeException e) {
//            throw new CustomException(SERVER_ERROR);
//        }
//    }
//
//    /**
//     * DislikedMember 싫어요한 멤버 조회 |
//     */
//    public List<DislikedMember> findDislikedMembersByMemberId(Long memberId) {
//
//        try {
//            List<DislikedMember> dislikedMembers = dislikedMemberRepository.findAllByMemberId(memberId);
//            return dislikedMembers;
//        } catch (RuntimeException e) {
//            throw new CustomException(SERVER_ERROR);
//        }
//    }
//
//    /**
//     * DislikedMember 싫어요한 멤버로 등록되어있는지 조회
//     * 멤버 식별자와 싫어요한 멤버 식별자를 인자로 받아서 이미 등록되어있다면 true로 반환한다.
//     */
//    public boolean isRegisteredDislikedMemberIdByMemberId(Long memberId, Long selectedMemberId) {
//
//        Optional<Long> registeredDislikedMemberId =
//                dislikedMemberRepository.isRegisteredDislikedMemberIdByMemberId(memberId, selectedMemberId);
//
//        if(registeredDislikedMemberId.isPresent()) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * DislikedMember 싫어요한 멤버 저장 |
//     * 멤버의 싫어요한 멤버를 저장한다. 이미 싫어요한 멤버(중복여부)인지 확인하고 중복 시 409(Conflict)를 던진다.
//     * 싫어요한 멤버를 저장 중에 문제가 발생하면 500(Internal Server Error)를 던진다.
//     */
//    @Transactional
//    public Long saveDislikedMember(Long memberId, Long DislikedMemberId) {
//        isExistingDislikedMember(memberId, DislikedMemberId);
//
//        Member loginMember = memberService.findById(memberId);
//        memberService.findById(DislikedMemberId);
//        matchingInfoService.findByMemberId(DislikedMemberId);
//
//        try {
//            DislikedMember DislikedMember = builder()
//                    .loginMember(loginMember)
//                    .dislikedMemberId(DislikedMemberId)
//                    .build();
//
//            dislikedMemberRepository.save(DislikedMember);
//            return DislikedMember.getId();
//        } catch (RuntimeException e) {
//            throw new CustomException(SERVER_ERROR);
//        }
//    }
//
//    /**
//     * 중복 싫어요한 멤버 여부 확인 |
//     * 싫어요한 멤버의 중복 여부를 확인하고, 이미 싫어요한 멤버라면 409(Conflict)를 던진다.
//     * 만약 조회 중에 에러가 발생하면 500(Internal Server Error)를 던진다.
//     */
//    private void isExistingDislikedMember(Long memberId, Long dislikedMemberId) {
//
//        List<Long> dislikedMemberIds = dislikedMemberRepository.findDislikedMembersByMemberId(memberId);
//
//        for(Long id : dislikedMemberIds) {
//            if(id == dislikedMemberId) {
//                throw new CustomException(ExceptionCode.DUPLICATE_DISLIKED_MEMBER);
//            }
//        }
//    }
//
//    /**
//     * 싫어요한 멤버 삭제
//     */
//    @Transactional
//    public void deleteDislikedMember(Long loginMemberId, Long dislikedMemberId) {
//        memberService.findById(loginMemberId);
//
//        try {
//            dislikedMemberRepository.deleteDislikedMember(loginMemberId, dislikedMemberId);
//        } catch (RuntimeException e) {
//            throw new CustomException(SERVER_ERROR);
//        }
//    }
//
//    /**
//     * DislikedMembers 삭제
//     * 회원 탈퇴 시 사용한다. selectedMemberId가 탈퇴한 멤버 식별자일 경우 삭제한다.
//     */
//    @Transactional
//    public void deleteDislikedMembers(Long memberId) {
//        try {
//            dislikedMemberRepository.deleteDislikedMembers(memberId);
//        } catch (RuntimeException e) {
//            throw new CustomException(SERVER_ERROR);
//        }
//    }
//}
