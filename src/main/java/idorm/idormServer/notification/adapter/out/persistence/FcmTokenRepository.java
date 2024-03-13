package idorm.idormServer.notification.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import idorm.idormServer.auth.entity.RoleType;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.notification.entity.FcmToken;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

	Optional<FcmToken> findByMemberId(Long memberId);

	boolean existsByMemberId(Long memberId);

	@Modifying
	void deleteAllByMemberId(Long memberId);

	@Modifying
	void deleteAllByUpdatedAtBefore(LocalDateTime expiredTime);

	@Query(value = "SELECT * "
		+ "FROM fcm_token F "
		+ "JOIN member M ON F.member_id = M.member_id "
		+ "JOIN matching_info MI ON M.matching_info_id = MI.matching_info_id "
		+ "WHERE MI.dorm_category = :dormCategory", nativeQuery = true)
	List<FcmToken> findByDormCategory(@Param("dormCategory") DormCategory dormCategory);

	@Query(value = "SELECT * "
		+ "FROM fcm_token F "
		+ "JOIN member M ON F.member_id = M.member_id "
		+ "WHERE M.member_id IN :memberIds", nativeQuery = true)
	List<FcmToken> findByMemberIds(List<Long> memberIds);

	@Query(value = "SELECT * "
		+ "FROM fcm_token F "
		+ "JOIN member M ON F.member_id = M.member_id "
		+ "where M.role_type = :roleType", nativeQuery = true)
	List<FcmToken> findByAdmins(@Param("roleType") RoleType roleType);
}