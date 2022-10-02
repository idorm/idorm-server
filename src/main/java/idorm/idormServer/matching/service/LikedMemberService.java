package idorm.idormServer.matching.service;

import idorm.idormServer.exceptions.http.ConflictException;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.matching.domain.LikedMember;
import idorm.idormServer.matching.repository.LikedMemberRepository;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
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
public class LikedMemberService {

    private final LikedMemberRepository likedMemberRepository;
    private final MemberService memberService;
    private final MatchingInfoService matchingInfoService;

    /**
     * LikedMember 좋아요한 멤버 조회 |
     * Member 식별자에 의해 조회되는 좋아요한 멤버의 전체 식별자를 조회합니다.
     */
    public List<Long> findLikedMembers(Long memberId) {
        log.info("IN PROGRESS | LikedMember 좋아요한 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);

        try {
            List<Long> likedMembers = likedMemberRepository.findLikedMembersByMemberId(memberId);
            log.info("COMPLETE | LikedMember 좋아요한 멤버 전체 조회 At " + LocalDateTime.now() + " | " + memberId);
            return likedMembers;
        } catch (Exception e) {
            throw new InternalServerErrorException("LikedMember 좋아요한 멤버 전체 조회 중 서버 에러 발생", e);
        }
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
        } catch (Exception e) {
            throw new InternalServerErrorException("LikedMember save 중 서버 에러 발생", e);
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
                throw new ConflictException("이미 좋아요한 멤버입니다.");
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

        // 삭제할 좋아요한 멤버가 존재하는지 확인하기 위한 코드
        memberService.findById(loginMemberId);
        List<Long> likedMemberIds = likedMemberRepository.findLikedMembersByMemberId(loginMemberId);

        boolean isExistLikedMember = false;

        for(Long id : likedMemberIds) {
            if(id == likedMemberId) {
                isExistLikedMember = true;
            }
        }

        if(isExistLikedMember == false) {
            throw new ConflictException("해당 아이디의 좋아요한 멤버는 존재하지 않기 때문에 삭제할 수 없습니다.");
        }

        try {
            likedMemberRepository.deleteLikedMember(loginMemberId, likedMemberId);
            log.info("COMPLETE | LikedMember 좋아요한 멤버 삭제 At " + LocalDateTime.now()
                    + " | 로그인 멤버 식별자: " + loginMemberId + " | 좋아요한 멤버 식별자 : " + likedMemberId);
        } catch (Exception e) {
            throw new InternalServerErrorException("LikedMember 좋아요한 멤버 삭제 중 서버 에러 발생", e);
        }
    }
}
