package idorm.idormServer.repository;

import idorm.idormServer.domain.MatchingInfo;
import idorm.idormServer.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchingInfoRepository extends JpaRepository<MatchingInfo, Long> {
}
