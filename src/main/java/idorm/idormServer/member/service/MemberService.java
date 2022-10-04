package idorm.idormServer.member.service;

import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.exceptions.http.ConflictException;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.exceptions.http.NotFoundException;
import idorm.idormServer.exceptions.http.UnauthorizedException;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;

    /**
     * Member 저장 |
     * 멤버를 저장한다. 중복여부를 확인하고 중복 시 409(Conflict)를 던진다.
     * 멤버를 저장 중에 문제가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public Long save(String email, String password) {

        log.info("IN PROGRESS | Member 저장 At " + LocalDateTime.now() + " | " + email);

        isExistingEmail(email);

        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        memberRepository.save(member);

        log.info("COMPLETE | Member 저장 At " + LocalDateTime.now() + " | " + member.getEmail());
        return member.getId();
    }

    /**
     * 중복 멤버메일 존재 여부 확인 |
     * 멤버메일의 존재 여부를 확인하고, 이미 사용 중인 멤버메일이라면 409(Conflict)를 던진다. 만약 조회 중에 에러가 발생하면
     * 500(Internal Server Error)을 던진다.
     */
    private void isExistingEmail(String email) {

        log.info("IN PROGRESS | Member 중복 여부 확인 At " + LocalDateTime.now() + " | " + email);

        Optional<Long> foundMember = memberRepository.findMemberIdByEmail(email);

        if (foundMember.isPresent()) {
            throw new ConflictException("이미 존재하는 회원입니다.");
        }

        log.info("COMPLETE | Member 중복 여부 확인 At " + LocalDateTime.now() + " | " + email);
    }

    /**
     * Member 단건 조회 |
     * 식별자로 멤버를 조회합니다. 찾을 수 없는 식별자라면 404(Not Found)를 던진다.
     */
    public Member findById(Long memberId) {

        log.info("IN PROGRESS | Member 단건 조회 At " + LocalDateTime.now() + " | " + memberId);

        Optional<Member> member = memberRepository.findById(memberId);

        if(member.isEmpty()) {
            throw new UnauthorizedException("해당 id의 멤버가 존재하지 않습니다.");
        }

        log.info("COMPLETE | Member 단건 조회 At " + LocalDateTime.now() + " | " + member.get().getEmail());
        return member.get();
    }

    /**
     * Member 전체 조회 |
     * 관리자 계정에서 사용한다. 조회되는 멤버가 없다면 404(Not Found)를 던진다.
     */
    public List<Member> findAll() {

        log.info("IN PROGRESS | Member 전체 조회 At " + LocalDateTime.now());

        List<Member> foundAllMembers = memberRepository.findAll();

        if(foundAllMembers.isEmpty()) {
            throw new NotFoundException("조회할 멤버가 존재하지 않습니다.");
        }

        log.info("COMPLETE | Member 전체 조회 At " + LocalDateTime.now() + " | Member 수: " + foundAllMembers.size());
        return foundAllMembers;
    }

    /**
     * Member 이메일로 조회 |
     */
    public Member findByEmail(String email) {

        log.info("IN PROGRESS | Member 이메일로 조회 At " + LocalDateTime.now() + " | " + email);

        if(email.equals("idorm")) {
            Optional<Member> adminMember = memberRepository.findById(1L);
            return adminMember.get();
        }

        emailService.findByEmail(email);

        Optional<Long> foundMemberId = memberRepository.findMemberIdByEmail(email);

        if(foundMemberId.isEmpty()) {
            throw new NotFoundException("가입되지 않은 이메일입니다.");
        }

        Member member = findById(foundMemberId.get());
        log.info("COMPLETE | Member 이메일로 조회 At " + LocalDateTime.now() + " | " + email);
        return member;
    }

    /**
     * Member 식별자를 이메일로 조회 Optional
     */
    public Optional<Long> findByEmailOp(String email) {

        log.info("IN PROGRESS | Member 이메일로 Optional 조회 At " + LocalDateTime.now() + " | " + email);

        try {
            Optional<Long> foundMemberId = memberRepository.findMemberIdByEmail(email);
            log.info("COMPLETE | Member 이메일로 Optional 조회 At " + LocalDateTime.now() + " | " + email);
            return foundMemberId;
        } catch (Exception e) {
            throw new InternalServerErrorException("Member 이메일로 Optional 조회 중 서버 에러 발생", e);
        }
    }


    /**
     * Member 닉네임으로 조회 |
     */
    public Optional<Member> findByNickname(String nickname) {

        log.info("IN PROGRESS | Member 닉네임으로 조회 At " + LocalDateTime.now() + " | " + nickname);

        Optional<Member> foundMember = memberRepository.findByNickname(nickname);

        log.info("COMPLETE | Member 닉네임으로 조회 At " + LocalDateTime.now() + " | " + foundMember);
        return foundMember;
    }

    /**
     * Member 삭제 |
     * 식별자로 삭제할 멤버를 조회한다. 멤버를 찾지 못하면 404(Not Found)를 던진다.
     * 멤버 삭제 중 에러가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public void deleteMember(Long memberId) {

        log.info("IN PROGRESS | Member 삭제 At " + LocalDateTime.now() + " | " + memberId);
        Optional<Member> foundMember = memberRepository.findById(memberId);

        if (foundMember.isEmpty()) {
            throw new NotFoundException("삭제할 멤버를 찾을 수 없습니다.");
        }

        try {
            memberRepository.delete(findById(memberId));
            log.info("COMPLETE | Member 삭제 At " + LocalDateTime.now() + " | " + memberId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Member 삭제 중 서버 에러 발생", e);
        }
    }

    /**
     * Member 비밀번호 수정 |
     */
    @Transactional
    public void updatePassword(Long memberId, String password) {

        log.info("IN PROGRESS | Member 비밀번호 변경 At " + LocalDateTime.now() + " | " + memberId);

        Optional<Member> foundMember = memberRepository.findById(memberId);

        if(foundMember.isEmpty()) {
            throw new UnauthorizedException("비밀번호를 변경할 멤버를 찾을 수 없습니다.");
        }

        try {
            foundMember.get().updatePassword(password);
            memberRepository.save(foundMember.get());
            log.info("COMPLETE | Member 비밀번호 변경 At " + LocalDateTime.now() + " | " + memberId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Member 비밀번호 변경 중 서버 에러 발생", e);
        }
    }

    /**
     * Member 닉네임 수정 |
     */
    @Transactional
    public void updateNickname(Long memberId, String nickname) {

        log.info("IN PROGRESS | Member 삭제 At " + LocalDateTime.now() + " | " + memberId);

        Optional<Member> foundMember = memberRepository.findById(memberId);

        if(foundMember.isEmpty()) {
            throw new UnauthorizedException("닉네임을 변경할 멤버를 찾을 수 없습니다.");
        }

        try {
            foundMember.get().updateNickname(nickname);
            memberRepository.save(foundMember.get());

            log.info("COMPLETE | Member 삭제 At " + LocalDateTime.now() + " | " + memberId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Member 닉네임 변경 중 서버 에러 발생", e);
        }
    }

    /**
     * Member 매칭정보 수정 |
     */
    @Transactional
    public void updateMatchingInfo(Member member, MatchingInfo matchingInfo) {
        log.info("IN PROGRESS | Member 매칭정보 수정 At " + LocalDateTime.now());

        try {
            member.updateMatchingInfo(matchingInfo);
            memberRepository.save(member);
        } catch (Exception e) {
            throw new InternalServerErrorException("Member 매칭정보 수정 중 서버 에러 발생", e);
        }

        log.info("COMPLETE | Member 매칭정보 수정 At " + LocalDateTime.now());
    }

    /**
     * Member 매칭정보 삭제 |
     */
    @Transactional
    public void deleteMatchingInfo(Member member) {

        log.info("IN PROGRESS | Member 매칭정보 삭제 At " + LocalDateTime.now() + " | " + member.getEmail());

        try {
            member.deleteMatchingInfo();
            memberRepository.save(member);
            log.info("COMPLETE | Member 매칭정보 삭제 At " + LocalDateTime.now() + " | " + member.getEmail());
        } catch (Exception e) {
            throw new InternalServerErrorException("Member 매칭정보 삭제 중 서버 에러 발생", e);
        }
    }
}
