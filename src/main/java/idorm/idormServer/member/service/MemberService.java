package idorm.idormServer.member.service;

import idorm.idormServer.community.service.PostService;
import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.exception.CustomException;

import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import idorm.idormServer.photo.domain.Photo;
import idorm.idormServer.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
     * Member 저장 |
     * 멤버를 저장한다. 중복여부를 확인하고 중복 시 409(Conflict)를 던진다.
     * 멤버를 저장 중에 문제가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public Long save(String email, String password, String nickname) {

        isExistingEmail(email);
        isDuplicateNickname(nickname);
        try {

            Member member = Member.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .build();

            memberRepository.save(member);
            return member.getId();
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Member 닉네임 중복 여부 체크 |
     */
    private void isDuplicateNickname(String nickname) {
        Optional<Member> foundMember = memberRepository.findByNickname(nickname);
        if(foundMember.isPresent()) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }
    }

    /**
     * Member 프로필 사진 저장 |
     * 멤버의 프로필 사진을 저장한다. 저장할 파일이 존재 한다면 파일 이름을 부여하여 저장한다. 저장 전에 이미 저장된 프로필 사진이 존재한다면 DB에는 중복으로
     * 생성하지 않고, 업데이트 하도록 한다.
     */
    @Transactional
    public Long saveProfilePhoto(Long memberId, MultipartFile photo) {

        Member foundMember = findById(memberId);

        String[] memberEmail = foundMember.getEmail().split("[@]");
        String memberEmailSplit = memberEmail[0];
        String fileName = memberEmailSplit + photo.getContentType().replace("image/", ".");

        Optional<Photo> foundPhoto = photoService.findOneByFileName(fileName);

        if(foundPhoto.isPresent()) {
            photoService.deleteProfilePhotos(foundMember);
        }

        Photo savedPhoto = photoService.saveProfilePhoto(foundMember, fileName, photo);

        try {
            foundMember.updatePhoto(savedPhoto);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
        return foundMember.getId();
    }

    /**
     * 중복 멤버메일 존재 여부 확인 |
     * 멤버메일의 존재 여부를 확인하고, 이미 사용 중인 멤버메일이라면 409(Conflict)를 던진다. 만약 조회 중에 에러가 발생하면
     * 500(Internal Server Error)을 던진다.
     */
    private void isExistingEmail(String email) {

        Optional<Long> foundMember = memberRepository.findMemberIdByEmail(email);

        if (foundMember.isPresent()) {
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

        emailService.findByEmail(email);

        Member foundMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        return foundMember;
    }

    /**
     * Member 식별자를 이메일로 조회 Optional
     */
    public Optional<Long> findByEmailOp(String email) {

        try {
            Optional<Long> foundMemberId = memberRepository.findMemberIdByEmail(email);
            return foundMemberId;
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Member 삭제 |
     * 식별자로 삭제할 멤버를 조회한다. 멤버를 찾지 못하면 404(Not Found)를 던진다.
     * 멤버 삭제 중 에러가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public void deleteMember(Member member) {

        Email foundEmail = emailService.findByEmail(member.getEmail());
        emailService.deleteById(foundEmail.getId());
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
     * 멤버 식별자를 통해 관련된 멤버 정보를 조회하여 멤버 프로필 사진을 삭제한다.저장된 프로필 사진이 존재하지 않는다면 FILE_NOT_FOUND 예외를 던진다.
     */
    @Transactional
    public void deleteMemberProfilePhoto(Member member) {

        photoService.isExistProfilePhoto(member);
        photoService.deleteProfilePhotos(member);
    }

    /**
     * Member 비밀번호 수정 |
     */
    @Transactional
    public void updatePassword(Member member, String password) {

        member.updatePassword(password);
        try {
            memberRepository.save(member);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Member 닉네임 수정 시간 확인 |
     * 닉네임 변경은 30일 이후로만 가능합니다.
     */
    private void checkNicknameUpdatedAt(Member member) {

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

        isDuplicateNickname(nickname);

        try {
            member.updateNickname(nickname);
            memberRepository.save(member);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Member 닉네임 수정 |
     */
    @Transactional
    public void updateNickname(Member member, String nickname) {

        if(member.getNickname().equals(nickname)) {
            throw new CustomException(DUPLICATE_SAME_NICKNAME);
        }

        isDuplicateNickname(nickname);
        checkNicknameUpdatedAt(member);

        member.updateNickname(nickname);
        member.updateNicknameUpdatedAt(LocalDate.now());
        try {
            memberRepository.save(member);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }
}
