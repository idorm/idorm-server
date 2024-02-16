package idorm.idormServer.auth.repository;

import idorm.idormServer.auth.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);
}
