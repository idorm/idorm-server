package idorm.idormServer.auth.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {

	Optional<RefreshTokenJpaEntity> findByMemberId(Long memberId);

	void deleteAllByMemberId(Long memberId);
}
