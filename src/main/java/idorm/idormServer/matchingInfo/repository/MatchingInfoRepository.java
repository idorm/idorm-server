package idorm.idormServer.matchingInfo.repository;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingInfoRepository extends JpaRepository<MatchingInfo, Long> {
}
