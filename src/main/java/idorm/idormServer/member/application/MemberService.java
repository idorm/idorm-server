package idorm.idormServer.member.service;

import idorm.idormServer.auth.dto.AuthInfo;
import idorm.idormServer.auth.encryptor.EncryptorI;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.domain.MemberStatus;
import idorm.idormServer.member.domain.Nickname;
import idorm.idormServer.member.domain.Password;
import idorm.idormServer.member.dto.MemberProfileResponse;
import idorm.idormServer.member.dto.NicknameUpdateRequest;
import idorm.idormServer.member.dto.PasswordUpdateRequest;
import idorm.idormServer.member.dto.SignupRequest;
import idorm.idormServer.member.repository.MemberRepository;
import idorm.idormServer.email.service.EmailService;
import java.time.Clock;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static idorm.idormServer.common.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final EncryptorI encryptor;
    private final Clock clock;

    @Transactional
    public void signUp(final SignupRequest signupRequest) {
        validate(signupRequest);

        Member member = Member.builder()
                .email(signupRequest.email())
                .password(Password.of(encryptor, signupRequest.password()))
                .nickname(new Nickname(signupRequest.nickname()))
                .build();

        emailService.useEmail(signupRequest.email());
        memberRepository.save(member);
    }

    public MemberProfileResponse getMemberInfo(final AuthInfo authInfo) {
        final Member member = memberRepository.findByIdAndMemberStatusIsActive(authInfo.getId())
                .orElseThrow(() -> new CustomException(null, MEMBER_NOT_FOUND));

        return MemberProfileResponse.of(member);
    }

    @Transactional
    public void editNickname(final AuthInfo authInfo, final NicknameUpdateRequest nicknameUpdateRequest) {
        Member member = memberRepository.findByIdAndMemberStatusIsActive(authInfo.getId())
                .orElseThrow(() -> new CustomException(null, MEMBER_NOT_FOUND));

        Nickname newNickname = new Nickname(nicknameUpdateRequest.nickname());

        validate(member.getNickname(), newNickname);
        member.updateNickname(newNickname);
    }

    @Transactional
    public void editPassword(final PasswordUpdateRequest passwordUpdateRequest) {
        emailService.validateUpdatePassword(passwordUpdateRequest.email());
        Password password = new Password(passwordUpdateRequest.password());

        Member member = memberRepository.findByEmailAndMemberStatusIsActive(passwordUpdateRequest.email())
                .orElseThrow(() -> new CustomException(null, MEMBER_NOT_FOUND));
        member.updatePassword(password);
    }

    private void validate(final SignupRequest signupRequest) {
        emailService.validateNewMember(signupRequest.email());
        validateUniqueEmail(signupRequest.email());
        validateUniqueNickname(new Nickname(signupRequest.nickname()));
    }

    private void validate(final Nickname previousNickname, final Nickname newNickname) {
        previousNickname.validateValidUpdate(LocalDateTime.now(clock));
        validateUniqueNickname(newNickname);
    }

    private void validateUniqueEmail(String email) {
        if (memberRepository.existsByEmailAndMemberStatus(email, MemberStatus.ACTIVE)) {
            throw new CustomException(null, DUPLICATE_EMAIL);
        }
    }

    private void validateUniqueNickname(Nickname nickname) {
        if (memberRepository.existsMemberByNicknameAndMemberStatusIsActive(nickname)) {
            throw new CustomException(null, DUPLICATE_NICKNAME);
        }
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
     * FCM용 팀 일정 대상자 회원 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Member> findTeamTargetMembers(List<Long> memberIds) {
        try {
            List<Member> members = new ArrayList<>();
            for (Long memberId : memberIds) {
                Optional<Member> member = memberRepository.findByIdAndIsDeletedIsFalseAndFcmTokenIsNotNull(memberId);
                member.ifPresent(members::add);
            }
            return members;
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
}