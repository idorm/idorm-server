package idorm.idormServer.member.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import idorm.idormServer.member.entity.Member;
import idorm.idormServer.member.entity.MemberStatus;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByNicknameValueAndMemberStatus(String nickname, MemberStatus memberStatus);

	Optional<Member> findByEmailAndPasswordValueAndMemberStatus(String email, String password,
		MemberStatus memberStatus);

	Optional<Member> findByIdAndMemberStatus(Long id, MemberStatus memberStatus);

	Optional<Member> findByEmailAndMemberStatus(String email, MemberStatus memberStatus);
}
