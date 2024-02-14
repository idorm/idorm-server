//package idorm.idormServer.member.service;
//
//import idorm.idormServer.calendar.service.OfficialCalendarServiceFacade;
//import idorm.idormServer.matchingInfo.domain.MatchingInfo;
//import idorm.idormServer.matchingInfo.service.MatchingInfoService;
//import idorm.idormServer.matchingMate.service.MatchingMateService;
//import idorm.idormServer.email.domain.Email;
//import idorm.idormServer.member.domain.Member;
//import idorm.idormServer.member.dto.SignupRequest;
//import idorm.idormServer.member.dto.PasswordRequest;
//import idorm.idormServer.member.domain.MemberPhoto;
//import idorm.idormServer.email.service.EmailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Optional;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class MemberServiceFacade {
//
//    private final PasswordEncoder passwordEncoder;
//    private final MemberService memberService;
//    private final EmailService emailService;
//    private final MemberPhotoService memberPhotoService;
//    private final MatchingMateService matchingService;
//    private final MatchingInfoService matchingInfoService;
//    private final OfficialCalendarServiceFacade calendarServiceFacade;
//
//    public Member saveMember(SignupRequest request, Email email) {
//        Member member = memberService.save(request.toMemberEntity(email, passwordEncoder.encode(request.getPassword())));
//
//        emailService.updateIsJoined(email, member);
//        return member;
//    }
//
//    public void saveMemberPhoto(Member member, MultipartFile file) {
//        MemberPhoto memberPhoto = memberPhotoService.findByMember(member);
//        if (memberPhoto != null)
//            memberPhotoService.delete(memberPhoto);
//
//        memberPhotoService.createMemberPhoto(member, file);
//    }
//
//    public void updatePassword(Email email, PasswordRequest request) {
//        memberService.updatePassword(email.getMember(), passwordEncoder.encode(request.getPassword()));
//        emailService.updateIsPossibleUpdatePassword(email, false);
//    }
//
//    public void deleteMember(Member member) {
//
//        if (member.getRoomMateTeam() != null)
//            calendarServiceFacade.deleteTeamMember(member.getRoomMateTeam(), member);
//
//        matchingService.removeAllLikedMembersByDeletedMember(member);
//        matchingService.removeAllDislikedMembersByDeletedMember(member);
//
//        if (!member.getAllMatchingInfo().isEmpty()) {
//            matchingInfoService.deleteData(member);
//            Optional<MatchingInfo> matchingInfo = matchingInfoService.findByMemberOp(member);
//
//            matchingInfo.ifPresent(matchingInfoService::delete);
//        }
//
//        Email email = emailService.findEmailByMember(member);
//        emailService.deleteData(email);
//        emailService.delete(email);
//        memberService.delete(member);
//
//        if (!member.getAllMemberPhoto().isEmpty()) {
//            memberPhotoService.deleteFromS3(member);
//            MemberPhoto memberPhoto = memberPhotoService.findByMember(member);
//            if (memberPhoto != null)
//                memberPhotoService.delete(memberPhoto);
//        }
//    }
//}