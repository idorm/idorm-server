package idorm.idormServer.matchingMate.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import idorm.idormServer.matchingMate.entity.MatchingMate;

public interface MatchingMateRepository extends JpaRepository<MatchingMate, Long> {
}
