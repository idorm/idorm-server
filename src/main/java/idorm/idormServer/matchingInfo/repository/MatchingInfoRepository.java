package idorm.idormServer.matchingInfo.repository;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchingInfoRepository extends JpaRepository<MatchingInfo, Long> {

    Optional<MatchingInfo> findByMemberId(Long memberId);
}
