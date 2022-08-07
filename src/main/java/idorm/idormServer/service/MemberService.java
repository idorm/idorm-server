package idorm.idormServer.service;

import idorm.idormServer.domain.Member;
import idorm.idormServer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 생성
     */
    @Transactional
    public Long join(String email, String password) {
        validateDuplicateMember(email); // 중복 회원 검증
        Member member = new Member(email, password);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(String email) {
        Optional<Member> findMembers = memberRepository.findByEmail(email);
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 조회
     */
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NullPointerException("id가 존재하지 않습니다."));
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
        memberRepository.delete(findById(memberId));
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void updateMember(Long memberId, String password) {
        Member member = memberRepository.findById(memberId).get();
        member.updatePassword(password);
    }

}
