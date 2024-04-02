package idorm.idormServer.notification.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import idorm.idormServer.notification.entity.FcmToken;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long>, FcmTokenRepositoryCustom {

	Optional<FcmToken> findByMemberId(Long memberId);

	@Modifying
	void deleteAllByMemberId(Long memberId);

	@Modifying
	void deleteAllByUpdatedAtBefore(LocalDateTime expiredTime);
}