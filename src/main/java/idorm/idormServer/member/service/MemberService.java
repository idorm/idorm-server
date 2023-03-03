package idorm.idormServer.member.service;

import idorm.idormServer.community.service.PostLikedMemberService;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.exception.CustomException;

import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.dto.MemberSaveRequestDto;
import idorm.idormServer.member.repository.MemberRepository;
import idorm.idormServer.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final PhotoService photoService;
    private final PostService postService;
    private final PostLikedMemberService postLikedMemberService;

    @Value("${DB_USERNAME}")
    private String ENV_USERNAME;

    /**
     * 회원 DB에 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Member save(Member member) {
        try {
            return memberRepository.save(member);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 회원 생성 |
     */
    @Transactional
    public Member createMember(MemberSaveRequestDto request) {

        Member createdMember = request.toEntity();
        save(createdMember);
        return createdMember;
    }

    /**
     * 닉네임 중복 검사 |
     * 409(DUPLICATE_NICKNAME)
     * 500(SERVER_ERROR)
     */
    public void isExistingNickname(String nickname) {
        Boolean isExistNicknameResult = false;
        try {
            isExistNicknameResult = memberRepository.existsByNickname(nickname);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
        if(isExistNicknameResult) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }
    }

    /**
     * 회원 프로필 사진 저장 |
     * 이미 저장되어있는 프로필 사진이 있다면 삭제 후 저장한다.
     */
    @Transactional
    public void saveProfilePhoto(Member member, MultipartFile file) {

        if (member.getProfilePhoto() != null)
            photoService.deleteProfilePhoto(member);

        photoService.createProfilePhoto(member, file);
    }

    /**
     * 가입 메일 중복 검사 |
     * 409(DUPLICATE_EMAIL)
     * 500(SERVER_ERROR)
     */
    public void isExistingEmail(String email) {

        Boolean isExistEmailResult = null;
        try {
            isExistEmailResult = memberRepository.existsByEmail(email);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
        if (isExistEmailResult) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
    }

    /**
     * 회원 단건 조회 |
     * 404(MEMBER_NOT_FOUND)
     */
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }

    /**
     * [Admin] 회원 전체 조회 |
     * 400(MEMBER_NOT_FOUND)
     */
    public List<Member> findAll() {

        try {
            return memberRepository.findAll();
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Member 이메일로 조회 |
     * 400(MEMBER_NOT_FOUND)
     */
    public Member findByEmail(String email) {

        if(email.equals(ENV_USERNAME)) {
            Optional<Member> adminMember = memberRepository.findById(1L);
            return adminMember.get();
        }

        Member foundMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        return foundMember;
    }

    /**
     * Member 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteMember(Member member) {

        emailService.deleteEmail(emailService.findByEmail(member.getEmail()));

        if (member.getProfilePhoto() != null)
            photoService.deleteProfilePhoto(member);

        if (member.getPosts() != null)
            postService.removeMember(member);

        if (member.getPostLikedMembers() != null)
            postLikedMemberService.removeMember(member);

        try {
            memberRepository.delete(member);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 회원 프로필 사진 삭제 |
     */
    @Transactional
    public void deleteMemberProfilePhoto(Member member) {

        photoService.isExistProfilePhoto(member);
        photoService.deleteProfilePhoto(member);
    }

    /**
     * Member 비밀번호 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updatePassword(Member member, String password) {

        try {
            member.updatePassword(password);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Member 닉네임 수정 시간 확인 |
     * 닉네임 변경은 30일 이후로만 가능합니다. |
     * 409(CANNOT_UPDATE_NICKNAME)
     */
    private void validateNicknameUpdatedAt(Member member) {

        if(member.getNicknameUpdatedAt() != null) { // 닉네임이 한 번이라도 변경 되었다면
            LocalDate updatedDate = member.getNicknameUpdatedAt();
            LocalDate possibleUpdateTime = updatedDate.plusMonths(1);

            if(possibleUpdateTime.isAfter(LocalDate.now())) {
                throw new CustomException(CANNOT_UPDATE_NICKNAME);
            }
        }
    }

    /**
     * Member 관리자계정으로 닉네임 수정
     */
    @Transactional
    public void updateNicknameByAdmin(Member member, String nickname) {

        if(member.getNickname().equals(nickname)) {
            throw new CustomException(DUPLICATE_SAME_NICKNAME);
        }

        isExistingNickname(nickname);

        try {
            member.updateNickname(nickname);
            memberRepository.save(member);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Member 닉네임 수정 |
     * 409(DUPLICATE_SAME_NICKNAME)
     */
    @Transactional
    public void updateNickname(Member member, String nickname) {

        if(member.getNickname().equals(nickname)) {
            throw new CustomException(DUPLICATE_SAME_NICKNAME);
        }

        isExistingNickname(nickname);
        validateNicknameUpdatedAt(member);

        member.updateNickname(nickname);
        member.updateNicknameUpdatedAt(LocalDate.now());

        save(member);
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
            throw new CustomException(SERVER_ERROR);
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
            throw new CustomException(SERVER_ERROR);
        }
    }
}
