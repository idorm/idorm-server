package idorm.idormServer.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.domain.AuthInfo;
import idorm.idormServer.auth.application.port.out.EncryptPort;
import idorm.idormServer.member.application.port.in.MemberUseCase;
import idorm.idormServer.member.application.port.in.dto.MemberInfoResponse;
import idorm.idormServer.member.application.port.in.dto.NicknameUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.PasswordUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.SignupRequest;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements MemberUseCase {

    private final EncryptPort encryptorPort;

    @Override
    public void signUp(final SignupRequest request) {
        //     validate(signupRequest);
        //
        //     Member member = Member.builder()
        //             .email(signupRequest.email())
        //             .password(Password.of(encryptor, signupRequest.password()))
        //             .nickname(new Nickname(signupRequest.nickname()))
        //             .build();
        //
        //     emailService.useEmail(signupRequest.email());
        //     memberRepository.save(member);
    }

    @Override
    public MemberInfoResponse getInfo(final AuthInfo authInfo) {
        //     final Member member = memberRepository.findByIdAndMemberStatusIsActive(authInfo.getId())
        //             .orElseThrow(() -> new CustomException(null, MEMBER_NOT_FOUND));
        //
        //     return MemberProfileResponse.of(member);

        return null;
    }

    @Override
    public void editNickname(final AuthInfo authInfo, final NicknameUpdateRequest request) {
        //     Member member = memberRepository.findByIdAndMemberStatusIsActive(authInfo.getId())
        //             .orElseThrow(() -> new CustomException(null, MEMBER_NOT_FOUND));
        //
        //     Nickname newNickname = new Nickname(nicknameUpdateRequest.nickname());
        //
        //     validate(member.getNickname(), newNickname);
        //     member.updateNickname(newNickname);
    }

    @Override
    public void editPassword(final PasswordUpdateRequest request) {
        //     emailService.validateUpdatePassword(passwordUpdateRequest.email());
        //     Password password = new Password(passwordUpdateRequest.password());
        //
        //     Member member = memberRepository.findByEmailAndMemberStatusIsActive(passwordUpdateRequest.email())
        //             .orElseThrow(() -> new CustomException(null, MEMBER_NOT_FOUND));
        //     member.updatePassword(password);
    }

    @Override
    public void leave(final AuthInfo authInfo) {

    }

    // private void validate(final SignupRequest signupRequest) {
    //     emailService.validateNewMember(signupRequest.email());
    //     validateUniqueEmail(signupRequest.email());
    //     validateUniqueNickname(new Nickname(signupRequest.nickname()));
    // }
    //
    // private void validate(final Nickname previousNickname, final Nickname newNickname) {
    //     previousNickname.validateValidUpdate(LocalDateTime.now(clock));
    //     validateUniqueNickname(newNickname);
    // }
    //
    // private void validateUniqueEmail(String email) {
    //     if (memberRepository.existsByEmailAndMemberStatus(email, MemberStatus.ACTIVE)) {
    //         throw new CustomException(null, DUPLICATE_EMAIL);
    //     }
    // }
    //
    // private void validateUniqueNickname(Nickname nickname) {
    //     if (memberRepository.existsMemberByNicknameAndMemberStatusIsActive(nickname)) {
    //         throw new CustomException(null, DUPLICATE_NICKNAME);
    //     }
    // }

    // 기존 EmailService 존재 코드
    // TODO: Member 리팩 시 수정 코드
    //    public void validateNewMember(final String email) {
    //        Email mail = emailRepository.findByEmailAndEmailStatus(email, EmailStatus.VERIFIED)
    //                .orElseThrow(() -> new CustomException(null, UNAUTHORIZED_EMAIL));
    //
    //        mail.validateValidTime(LocalDateTime.now());
    //        validateNotSignUpEmail(mail);
    //    }
    //
    //    public void validateUpdatePassword(final String email) {
    //        Email mail = emailRepository.findByEmailAndEmailStatus(email, EmailStatus.RE_VERIFIED)
    //                .orElseThrow(() -> new CustomException(null, UNAUTHORIZED_EMAIL));
    //
    //        mail.validateValidTime(LocalDateTime.now());
    //        validateSignUpEmail(mail);
    //    }
    //
    //    @Transactional
    //    public void useEmail(final String email) {
    //        Email mail = emailRepository.findByEmail(email)
    //                .orElseThrow(() -> new CustomException(null, EMAIL_NOT_FOUND));
    //        mail.register();
    //    }
}