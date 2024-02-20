package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<TeamJpaEntity, Long> {

	@Query(value = "SELECT * "
		+ "FROM TeamJpaEntity t "
		+ "JOIN FETCH t.members m "
		+ "WHERE m.id = :memberId", nativeQuery = true)
	Optional<TeamJpaEntity> findByMemberIdWithOptional(Long memberId);

	@Query(value = "SELECT * "
		+ "FROM TeamJpaEntity t "
		+ "JOIN FETCH t.sleepoverCalendars sc "
		+ "JOIN FETCH t.teamCalendars tc "
		+ "JOIN t.members m "
		+ "WHERE m.id = :memberId", nativeQuery = true)
	Optional<TeamJpaEntity> findByMemberIdWithCalendarsAndMembers(Long memberId);

	@Query(value = "SELECT * "
		+ "FROM TeamJpaEntity t "
		+ "JOIN FETCH t.members m ", nativeQuery = true)
	Optional<TeamJpaEntity> findByMemberIdWithTeamMember(Long memberId);
}
