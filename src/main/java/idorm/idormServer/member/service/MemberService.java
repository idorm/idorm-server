package idorm.idormServer.member.service;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 저장
     */
    @Transactional
    public Long join(String email, String password) {
        log.info("IN PROGRESS | Member 저장 At " + LocalDateTime.now() + " | " + email);
        validateDuplicateMember(email); // 중복 회원 검증
        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();



        memberRepository.save(member);
        log.info("COMPLETE | Member 저장 At " + LocalDateTime.now() + " | " + member.getEmail());
        return member.getId();
    }

    private void validateDuplicateMember(String email) {
        log.info("IN PROGRESS | Member 중복 확인 At " + LocalDateTime.now() + " | " + email);
        Optional<Member> findMembers = memberRepository.findByEmail(email);
        if (!findMembers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다.");
        }
        log.info("COMPLETE | Member 중복 없음 확인 At " + LocalDateTime.now() + " | " + email);
    }

    /**
     * 회원 조회
     */
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("id가 존재하지 않습니다."));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findByNickname(String name) {
        return memberRepository.findByNickname(name);
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.findById(memberId).get().updateIsLeft(); // 회원 정보 업데이트
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void updateMember(Long memberId, String password, String nickname) {
        Member member = memberRepository.findById(memberId).get();
        member.updatePassword(password);
        member.updateNickname(nickname);
        memberRepository.save(member);
    }
    @Transactional
    public void updatePassword(Long memberId, String password) {
        Member member = memberRepository.findById(memberId).get();
        member.updatePassword(password);
        memberRepository.save(member);
    }

    @Transactional
    public void updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId).get();
        member.updateNickname(nickname);
        memberRepository.save(member);
    }

    @Transactional
    public void updateIsLeft(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        member.updateIsLeft();
        memberRepository.save(member);
    }

    @Transactional
    public void updateMatchingInfo(Member member, MatchingInfo matchingInfo) {
        log.info("START | Member Service updateMatchingInfo 저장 At " + LocalDateTime.now());
        member.updateMatchingInfo(matchingInfo);
        memberRepository.save(member);

        log.info("COMPLETE | Member Service updateMatchingInfo 저장 At " + LocalDateTime.now());
    }

    @Transactional
    public void deleteMatchingInfo(Member member) {
        member.deleteMatchingInfo();
        memberRepository.save(member);
    }
}
