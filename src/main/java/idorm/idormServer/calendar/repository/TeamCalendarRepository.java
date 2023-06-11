package idorm.idormServer.calendar.repository;

import idorm.idormServer.calendar.domain.TeamCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamCalendarRepository extends JpaRepository<TeamCalendar, Long> {

    Optional<TeamCalendar> findByIdAndIsDeletedIsFalse(Long id);

    @Query(value = "SELECT * " +
            "FROM team_calendar c " +
            "WHERE c.team_id = :teamId " +
            "AND (c.start_date LIKE :yearMonth " +
            "OR c.end_date LIKE :yearMonth) " +
            "AND c.is_deleted = 0", nativeQuery = true)
    List<TeamCalendar> findByTeamAndIsDeletedIsFalseAndDateLike(Long teamId, String yearMonth);
}
