package idorm.idormServer.notification.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import idorm.idormServer.notification.entity.FcmToken;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

	Optional<FcmToken> findByMemberId(Long memberId);

	boolean existsByMemberId(Long memberId);

	@Modifying
	void deleteAllByMemberId(Long memberId);

	@Modifying
	void deleteAllByUpdatedAtBefore(LocalDateTime expiredTime);
}
