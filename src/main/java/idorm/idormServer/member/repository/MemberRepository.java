package idorm.idormServer.member.repository;

import idorm.idormServer.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email); // 이메일로 유저 찾기

    Optional<Member> findByNickname(String nickname);
}