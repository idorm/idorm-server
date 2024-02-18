package idorm.idormServer.member.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberJpaEntity, Long> {

	boolean existsMemberByNicknameValueAndMemberStatusIsActive(NicknameEmbeddedEntity nickname);

	Optional<MemberJpaEntity> findByEmailAndPasswordValueAndMemberStatusIsActive(String email, String password);

	Optional<MemberJpaEntity> findByIdAndMemberStatusIsActive(Long id);

	Optional<MemberJpaEntity> findByEmailAndMemberStatusIsActive(String email);
}
