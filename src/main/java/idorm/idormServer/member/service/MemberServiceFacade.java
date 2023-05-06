package idorm.idormServer.member.service;

import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.matching.service.MatchingService;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.dto.MemberSaveRequestDto;
import idorm.idormServer.member.dto.MemberUpdatePasswordRequestDto;
import idorm.idormServer.photo.service.MemberPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceFacade {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final EmailService emailService;
    private final MemberPhotoService memberPhotoService;
    private final MatchingService matchingService;
    private final MatchingInfoService matchingInfoService;

    public Member saveMember(MemberSaveRequestDto request, Email email) {
        Member member = memberService.save(request.toMemberEntity(email, passwordEncoder.encode(request.getPassword())));

        emailService.updateIsJoined(email, member);
        return member;
    }

    public void saveMemberPhoto(Member member, MultipartFile file) {
        if (member.getMemberPhoto() != null)
            memberPhotoService.delete(member.getMemberPhoto());

        memberPhotoService.createMemberPhoto(member, file);
    }

    public void updatePassword(Email email, MemberUpdatePasswordRequestDto request) {
        memberService.updatePassword(email.getMember(), passwordEncoder.encode(request.getPassword()));
        emailService.updateIsPossibleUpdatePassword(email, false);
    }

    public void deleteMember(Member member) {
        matchingService.removeAllLikedMembersByDeletedMember(member);
        matchingService.removeAllDislikedMembersByDeletedMember(member);

        if (!member.getAllMatchingInfo().isEmpty()) {
            matchingInfoService.deleteData(member);
            if (member.getMatchingInfo() != null)
                matchingInfoService.delete(member.getMatchingInfo());
        }

        emailService.deleteData(member.getEmail());
        emailService.delete(member.getEmail());
        memberService.delete(member);

        if (!member.getAllMemberPhoto().isEmpty()) {
            memberPhotoService.deleteFromS3(member);
            memberPhotoService.delete(member.getMemberPhoto());
        }
    }
}
