package idorm.idormServer.matchingInfo.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import idorm.idormServer.matchingInfo.entity.MatchingInfo;

@Repository
public interface MatchingInfoRepository extends JpaRepository<MatchingInfo, Long>, MatchingInfoRepositoryCustom {

	Optional<MatchingInfo> findByMemberId(Long memberId);

	boolean existsByMemberId(Long memberId);

	@Modifying
	void deleteById(Long matchingInfoId);
}