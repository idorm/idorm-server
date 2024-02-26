package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import idorm.idormServer.calendar.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

	@Query(value = "SELECT * "
		+ "FROM TeamJpaEntity t "
		+ "JOIN FETCH t.members m "
		+ "WHERE m.id = :memberId", nativeQuery = true)
	Optional<Team> findByMemberIdWithOptional(Long memberId);

	@Query(value = "SELECT * "
		+ "FROM TeamJpaEntity t "
		+ "JOIN FETCH t.sleepoverCalendars sc "
		+ "JOIN FETCH t.teamCalendars tc "
		+ "JOIN t.members m "
		+ "WHERE m.id = :memberId", nativeQuery = true)
	Optional<Team> findByMemberIdWithCalendarsAndMembers(Long memberId);

	@Query(value = "SELECT * "
		+ "FROM TeamJpaEntity t "
		+ "JOIN FETCH t.members m ", nativeQuery = true)
	Optional<Team> findByMemberIdWithTeamMember(Long memberId);
}
