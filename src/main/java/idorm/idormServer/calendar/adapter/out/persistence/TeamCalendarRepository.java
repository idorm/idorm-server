package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import idorm.idormServer.calendar.entity.TeamCalendar;

@Repository
public interface TeamCalendarRepository extends JpaRepository<TeamCalendar, Long>, TeamCalendarCustomRepository {

	Optional<TeamCalendar> findByIdAndTeamId(Long teamCalendarId, Long teamId);

	List<TeamCalendar> findByTeamId(Long teamId);
}