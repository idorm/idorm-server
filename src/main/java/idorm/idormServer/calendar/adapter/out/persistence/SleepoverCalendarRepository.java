package idorm.idormServer.calendar.adapter.out.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SleepoverCalendarRepository extends
    JpaRepository<SleepoverCalendarJpaEntity, Long> {

  Optional<SleepoverCalendarJpaEntity> findByIdAndIsDeletedIsFalse(Long id);

  @Query(value = "SELECT * "
      + "FROM SleepoverCalendarJpaEntity s "
      + "JOIN FETCH s.participant p "
      + "WHERE p.memberId = :memberId "
      + "AND s.id = : sleepoverCalendarId", nativeQuery = true)
  Optional<SleepoverCalendarJpaEntity> findByIdAndMemberId(Long sleepoverCalendarId, Long memberId);

  @Query(value = "SELECT * "
      + "FROM SleepoverCalendarJpaEntity s "
      + "JOIN FETCH s.paticipant participant "
      + "WHERE participant.memberId = :memberId", nativeQuery = true)
  List<SleepoverCalendarJpaEntity> findByMemberId(Long memberId);
  @Query(value = "SELECT * "
      + "FROM SleepoverCalendarJpaEntity s "
      + "WHERE s.team_id = :teamId", nativeQuery = true)
  List<SleepoverCalendarJpaEntity> findByTeamId(Long teamId);

  @Query(value = "SELECT * " +
      "FROM SleepoverCalendarJpaEntity s " +
      "WHERE s.team_id = :teamId " +
      "AND DATE(s.start_date) <= CURRENT_DATE "+
      "AND DATE(s.end_date) >= CURRENT_DATE", nativeQuery = true)
  List<SleepoverCalendarJpaEntity> findByToday(Long teamId);

  @Query(value = "SELECT COUNT(s) "
      + "FROM SleepoverCalendarJpaEntity s "
      + "WHERE :newStartDate <= s.period.endDate "
      + "AND :newEndDate >= s.period.startDate "
      + "AND :memberId = s.participant.memberId", nativeQuery = true)
  Long countOverlappingDates(Long memberId, LocalDate newStartDate, LocalDate newEndDate);

  @Query(value = "SELECT * " +
      "FROM SleepoverCalendarJpaEntity s " +
      "JOIN FETCH s.period p " +
      "WHERE s.team_id = :teamId " +
      "AND (p.start_date LIKE :yearMonth " +
      "OR p.end_date LIKE :yearMonth)", nativeQuery = true)
  List<SleepoverCalendarJpaEntity> findByYearMonth(Long teamId, String yearMonth);

}