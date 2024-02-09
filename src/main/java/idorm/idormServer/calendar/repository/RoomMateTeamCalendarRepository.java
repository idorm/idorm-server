package idorm.idormServer.calendar.repository;

import idorm.idormServer.calendar.domain.TeamCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomMateTeamCalendarRepository extends JpaRepository<TeamCalendar, Long> {

    Optional<TeamCalendar> findByIdAndIsDeletedIsFalse(Long id);

    @Query(value = "SELECT * " +
            "FROM room_mate_team_calendar c " +
            "WHERE c.room_mate_team_id = :teamId " +
            "AND (c.start_date LIKE :yearMonth " +
            "OR c.end_date LIKE :yearMonth) " +
            "AND c.is_sleepover = :isSleepover " +
            "AND c.is_deleted = 0", nativeQuery = true)
    List<TeamCalendar> findTeamCalendars(Long teamId, String yearMonth, int isSleepover);

    @Query(value = "SELECT * " +
            "FROM room_mate_team_calendar c " +
            "WHERE c.is_deleted = 0 " +
            "AND c.room_mate_team_id = :teamId " +
            "AND ((str_to_date(c.start_date, '%Y-%m-%d') <= :startDate " +
            "AND :startDate <= str_to_date(c.end_date, '%Y-%m-%d')) " +
            "OR (str_to_date(c.start_date, '%Y-%m-%d') <= :endDate " +
            "AND :endDate <= str_to_date(c.end_date, '%Y-%m-%d'))) ", nativeQuery = true
    )
    List<TeamCalendar> findTeamCalendarsByDate(Long teamId, String startDate, String endDate);

    @Query(value = "SELECT * " +
            "FROM room_mate_team_calendar c " +
            "WHERE c.is_deleted = 0 " +
            "AND c.room_mate_team_id = :teamId " +
            "AND c.is_sleepover = 1 " +
            "AND (str_to_date(c.start_date, '%Y-%m-%d') <= str_to_date(DATE_ADD(NOW(), INTERVAL 9 HOUR), '%Y-%m-%d') " +
            "AND str_to_date(DATE_ADD(NOW(), INTERVAL 9 HOUR), '%Y-%m-%d') <= str_to_date(c.end_date, '%Y-%m-%d'));"
            , nativeQuery = true)
    List<TeamCalendar> findTodaySleepoverMembersByTeam(Long teamId);

    @Query(value = "SELECT * " +
            "FROM room_mate_team_calendar c " +
            "WHERE c.is_deleted = 0 " +
            "and (str_to_date(c.start_date, '%Y-%m-%d') = str_to_date(DATE_ADD(NOW(), INTERVAL 9 HOUR), '%Y-%m-%d'))"
            , nativeQuery = true)
    List<TeamCalendar> findTeamCalendarsByStartDateIsToday();
}
