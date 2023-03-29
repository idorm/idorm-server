package idorm.idormServer.member.service;

import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.exception.CustomException;

import idorm.idormServer.matching.service.MatchingService;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import idorm.idormServer.photo.service.MemberPhotoService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final MemberPhotoService memberPhotoService;
    private final MatchingService matchingService;
    private final MatchingInfoService matchingInfoService;

    /**
     * DB에 회원 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Member save(Member member) {
        try {
            return memberRepository.save(member);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(Member member) {
        try {
            member.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원 탈퇴 |
     */
    @Transactional
    public void deactivateMember(Member member) {
        matchingService.removeAllLikedMembersByDeletedMember(member);
        matchingService.removeAllDislikedMembersByDeletedMember(member);

        if (member.getMatchingInfo() != null) {
            matchingInfoService.deleteData(member.getMatchingInfo());
            matchingInfoService.delete(member.getMatchingInfo());
        }

        emailService.deleteData(member.getEmail());
        emailService.delete(member.getEmail());
        delete(member);

        if (member.getMemberPhoto() != null) {
            memberPhotoService.deleteFromS3(member);
            memberPhotoService.delete(member.getMemberPhoto());
        }
    }

    /**
     * 비밀번호 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updatePassword(Member member, String encodedPassword) {

        try {
            member.updatePassword(encodedPassword);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 닉네임 수정 |
     * 409(DUPLICATE_SAME_NICKNAME)
     */
    @Transactional
    public void updateNickname(Member member, String nickname) {

        try {
            member.updateNickname(nickname);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원 FCM 토큰 설정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateFcmToken(Member member, String fcmToken) {
        try {
            member.updateFcmToken(fcmToken);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원 FCM 토큰 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteFcmToken(Member member) {
        try {
            member.deleteFcmToken();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원 단건 조회 |
     * 404(MEMBER_NOT_FOUND)
     */
    public Member findById(Long memberId) {
        return memberRepository.findByIdAndIsDeletedIsFalse(memberId)
                .orElseThrow(() -> new CustomException(null, MEMBER_NOT_FOUND));
    }

    /**
     * 이메일로 회원 단건 조회 |
     * 404(MEMBER_NOT_FOUND)
     */
    public Member findByEmail(String email) {

        Email foundEmail = emailService.findMemberByEmail(email)
                .orElseThrow(() -> new CustomException(null, MEMBER_NOT_FOUND));

        return findById(foundEmail.getMember().getId());
    }

    /**
     * FCM용 전체 회원 조회 |
     * 404(MEMBER_NOT_FOUND)
     */
    public List<Member> findAll() {

        try {
            return memberRepository.findByIdNotAndIsDeletedIsFalseAndFcmTokenIsNotNull(1L);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 닉네임 중복 검사 |
     * 409(DUPLICATE_NICKNAME)
     * 500(SERVER_ERROR)
     */
    public void isExistingNickname(String nickname) {

        Boolean isExistNicknameResult = false;
        try {
            isExistNicknameResult = memberRepository.existsByNicknameAndIsDeletedIsFalse(nickname);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        if(isExistNicknameResult) {
            throw new CustomException(null, DUPLICATE_NICKNAME);
        }
    }

    /**
     * 닉네임 수정 시간 확인 |
     * 닉네임 변경은 30일 이후로만 가능합니다. |
     * 409(CANNOT_UPDATE_NICKNAME)
     */
    public void validateNicknameUpdatedAt(Member member) {

        if (member.getNicknameUpdatedAt() == null)
            return;

        LocalDateTime updatedDate = member.getNicknameUpdatedAt();
        LocalDateTime possibleUpdateTime = updatedDate.plusMonths(1);

        if(possibleUpdateTime.isAfter(LocalDateTime.now()))
            throw new CustomException(null, CANNOT_UPDATE_NICKNAME);
    }

    /**
     * 기존 닉네임 변경 닉네임 동일 여부 검증 |
     * 409(DUPLICATE_SAME_NICKNAME)
     */
    public void validateUpdateNicknameIsChanged(Member member, String newNickname) {
        if (member.getNickname().equals(newNickname))
            throw new CustomException(null, DUPLICATE_SAME_NICKNAME);
    }
}
