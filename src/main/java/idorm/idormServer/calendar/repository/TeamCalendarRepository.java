package idorm.idormServer.calendar.repository;

import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TeamCalendarRepository extends JpaRepository<TeamCalendar, Long> {

    Optional<TeamCalendar> findByIdAndIsDeletedIsFalse(Long id);

    @Query(value = "SELECT * " +
            "FROM team_calendar c " +
            "WHERE c.team_id = :teamId " +
            "AND (c.start_date LIKE :yearMonth " +
            "OR c.end_date LIKE :yearMonth) " +
            "AND c.is_sleepover = :isSleepover " +
            "AND c.is_deleted = 0", nativeQuery = true)
    List<TeamCalendar> findTeamCalendars(Long teamId, String yearMonth, int isSleepover);

    @Query(value = "SELECT * " +
            "FROM team_calendar c " +
            "WHERE c.is_deleted = 0 " +
            "AND c.team_id = :teamId " +
            "AND ((str_to_date(c.start_date, '%Y-%m-%d') <= :startDate " +
            "AND :startDate <= str_to_date(c.end_date, '%Y-%m-%d')) " +
            "OR (str_to_date(c.start_date, '%Y-%m-%d') <= :endDate " +
            "AND :endDate <= str_to_date(c.end_date, '%Y-%m-%d'))) ", nativeQuery = true
    )
    List<TeamCalendar> findTeamCalendarsByDate(Long teamId, String startDate, String endDate);
}
