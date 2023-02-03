package idorm.idormServer.member.service;

import idorm.idormServer.community.service.PostService;
import idorm.idormServer.email.domain.Email;
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

    @Value("${DB_USERNAME}")
    private String ENV_USERNAME;

    /**
     * 회원 DB에 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Member save(Member member) {
        try {
            Member savedMember = memberRepository.save(member);
            return savedMember;
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
        return save(createdMember);
    }

    /**
     * 닉네임 중복 검사 |
     * 409(DUPLICATE_NICKNAME)
     */
    public void isExistingNickname(String nickname) {
        boolean isExistNicknameResult = memberRepository.existsByNickname(nickname);
        if(isExistNicknameResult) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }
    }

    /**
     * Member 프로필 사진 저장 |
     * 멤버의 프로필 사진을 저장한다. 저장할 파일이 존재 한다면 파일 이름을 부여하여 저장한다. 저장 전에 이미 저장된 프로필 사진이 존재한다면 DB에는 중복으로
     * 생성하지 않고, 업데이트 하도록 한다.
     */
    @Transactional
    public void saveProfilePhoto(Member member, MultipartFile file) {

        String fileName = member.getId() + file.getContentType().replace("image/", ".");

        if (member.getProfilePhoto() != null) {
            photoService.deleteProfilePhotos(member);
        }

        photoService.createProfilePhoto(member, file);
    }

    /**
     * 가입 메일 중복 검사 |
     * 409(DUPLICATE_EMAIL)
     */
    public void isExistingEmail(String email) {

        boolean isExistEmailResult = memberRepository.existsByEmail(email);
        if (isExistEmailResult) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
    }

    /**
     * Member 단건 조회 |
     * 식별자로 멤버를 조회합니다. 찾을 수 없는 식별자라면 404(Not Found)를 던진다.
     */
    public Member findById(Long memberId) {

        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        return foundMember;
    }

    /**
     * Member 전체 조회 |
     * 관리자 계정에서 사용한다. 조회되는 멤버가 없다면 404(Not Found)를 던진다.
     */
    public List<Member> findAll() {

        List<Member> foundMembers = memberRepository.findAll();

        if(foundMembers.isEmpty()) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        return foundMembers;
    }

    /**
     * Member 이메일로 조회 |
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

        Email foundEmail = emailService.findByEmail(member.getEmail());
        emailService.deleteEmail(foundEmail);
        photoService.deleteProfilePhotos(member);
        postService.updateMemberNullFromPost(member);
        try {
            memberRepository.delete(member);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Member 프로필 포토 삭제 |
     */
    @Transactional
    public void deleteMemberProfilePhoto(Member member) {

        photoService.isExistProfilePhoto(member);
        photoService.deleteProfilePhotos(member);
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
}
