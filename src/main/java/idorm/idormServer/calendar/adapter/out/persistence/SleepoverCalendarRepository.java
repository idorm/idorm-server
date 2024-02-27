package idorm.idormServer.calendar.adapter.out.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import idorm.idormServer.calendar.entity.SleepoverCalendar;

public interface SleepoverCalendarRepository extends JpaRepository<SleepoverCalendar, Long> {

	// Optional<SleepoverCalendar> findByIdAndIsDeletedIsFalse(Long id);

	@Query(value = "SELECT * "
		+ "FROM SleepoverCalendar s "
		+ "WHERE s.memberId = :memberId "
		+ "AND s.sleepover_calendar_id = :sleepoverCalendarId", nativeQuery = true)
	Optional<SleepoverCalendar> findByIdAndMemberId(Long sleepoverCalendarId, Long memberId);

	@Query(value = "SELECT * "
		+ "FROM SleepoverCalendar s "
		+ "JOIN FETCH s.paticipant participant "
		+ "WHERE participant.memberId = :memberId", nativeQuery = true)
	List<SleepoverCalendar> findByMemberId(Long memberId);

	@Query(value = "SELECT * "
		+ "FROM SleepoverCalendar s "
		+ "WHERE s.team_id = :teamId", nativeQuery = true)
	List<SleepoverCalendar> findByTeamId(Long teamId);

	@Query(value = "SELECT * " +
		"FROM SleepoverCalendar s " +
		"WHERE s.team_id = :teamId " +
		"AND DATE(s.start_date) <= CURRENT_DATE " +
		"AND DATE(s.end_date) >= CURRENT_DATE", nativeQuery = true)
	List<SleepoverCalendar> findByToday(Long teamId);

	@Query(value = "SELECT COUNT(s) "
		+ "FROM SleepoverCalendar s "
		+ "WHERE :newStartDate <= s.period.endDate "
		+ "AND :newEndDate >= s.period.startDate "
		+ "AND :memberId = s.participant.memberId", nativeQuery = true)
	Long countOverlappingDates(Long memberId, LocalDate newStartDate, LocalDate newEndDate);

	@Query(value = "SELECT * " +
		"FROM SleepoverCalendar s " +
		"JOIN FETCH s.period p " +
		"WHERE s.team_id = :teamId " +
		"AND (p.start_date LIKE :yearMonth " +
		"OR p.end_date LIKE :yearMonth)", nativeQuery = true)
	List<SleepoverCalendar> findByYearMonth(Long teamId, String yearMonth);

}