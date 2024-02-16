package idorm.idormServer.member.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import idorm.idormServer.member.domain.MemberStatus;
import idorm.idormServer.member.domain.Nickname;

public interface MemberRepository extends JpaRepository<MemberJpaEntity, Long> {

	boolean existsByEmailAndMemberStatus(String email, MemberStatus memberStatus);

	boolean existsMemberByNicknameAndMemberStatusIsActive(Nickname nickname);

	Optional<MemberJpaEntity> findByEmailAndPasswordValueAndMemberStatusIsActive(String email, String password);

	Optional<MemberJpaEntity> findByIdAndMemberStatusIsActive(Long id);
}
