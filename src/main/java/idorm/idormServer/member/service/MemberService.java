package idorm.idormServer.member.service;

import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.exception.CustomException;

import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    public MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

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
     * 회원 단건 조회 Optional |
     * 500(SERVER_ERROR)
     */
    public Optional<Member> findByIdOptional(Long memberId) {
        try {
            return memberRepository.findByIdAndIsDeletedIsFalse(memberId);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
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
     * FCM용 1 기숙사 회원 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Member> findAllOfDorm1() {

        try {
            return memberRepository.findByDormCategoryAndIdIsNotAndIsDeletedIsFalseAndFcmTokenIsNotNull(DormCategory.DORM1.getType(), 1L);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * FCM용 2 기숙사 회원 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Member> findAllOfDorm2() {

        try {
            return memberRepository.findByDormCategoryAndIdIsNotAndIsDeletedIsFalseAndFcmTokenIsNotNull(DormCategory.DORM2.getType(), 1L);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * FCM용 3 기숙사 회원 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Member> findAllOfDorm3() {

        try {
            return memberRepository.findByDormCategoryAndIdIsNotAndIsDeletedIsFalseAndFcmTokenIsNotNull(DormCategory.DORM3.getType(), 1L);
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

    /**
     * 관리자 대상 여부 검증 |
     * 403(FORBIDDEN_TARGET_ADMIN)
     */
    public void validateTargetAdmin(Member member) {
        if(member.getRoles().contains("ROLE_ADMIN"))
            throw new CustomException(null, FORBIDDEN_TARGET_ADMIN);
    }

    /**
     * 본인 대상 여부 검증 |
     * 400(ILLEGAL_ARGUMENT_SELF)
     */
    public void validateTargetSelf(Member loginMember, Member targetMember) {
        if (loginMember.equals(targetMember))
            throw new CustomException(null,ILLEGAL_ARGUMENT_SELF);
    }

    /**
     * 비밀번호 일치 여부 검증 |
     * 401(UNAUTHORIZED_PASSWORD)
     */
    public void validatePassword(Member member, String password) {
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(null, UNAUTHORIZED_PASSWORD);
        }
    }
}
