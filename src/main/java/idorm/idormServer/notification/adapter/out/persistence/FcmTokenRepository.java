package idorm.idormServer.notification.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmTokenJpaEntity, Long> {

	Optional<FcmTokenJpaEntity> findByMemberId(Long memberId);

	boolean existsByMemberId(Long memberId);
}
