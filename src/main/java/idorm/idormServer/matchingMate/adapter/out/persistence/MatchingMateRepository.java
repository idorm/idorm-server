package idorm.idormServer.matchingMate.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import idorm.idormServer.matchingMate.entity.MatchingMate;

@Repository
public interface MatchingMateRepository extends JpaRepository<MatchingMate, Long> {

	@Modifying
	void deleteAllByMatchingInfoForTargetId(Long matchingInfoId);

	@Modifying
	void deleteAllByOwnerId(Long memberId);
}
