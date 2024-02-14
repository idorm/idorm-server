package idorm.idormServer.matchingMate.adapter.out.persistence;

import idorm.idormServer.matchingMate.domain.MatchingMate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingMateRepository extends JpaRepository<MatchingMate, Long> {
}
