package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamCalendarRepository extends JpaRepository<TeamCalendarJpaEntity, Long> {

  @Query(value = "SELECT * "
      + "FROM TeamCalendarJpaEntity tc "
      + "JOIN FETCH tc.participants participants "
      + "WHERE tc.id = :teamCalendarId "
      + "AND tc.team_id = :teamId ", nativeQuery = true)
  Optional<TeamCalendarJpaEntity> findByIdAndTeamId(Long teamCalendarId, Long teamId);

  @Query(value = "SELECT * "
      + "FROM TeamCalendarJpaEntity tc "
      + "JOIN FETCH tc.participants participants "
      + "WHERE participants.memberId = :memberId "
      + "AND tc.id = :teamCalendarId ", nativeQuery = true)
  Optional<TeamCalendarJpaEntity> findByIdAndMemberId(Long teamCalendarId, Long memberId);

  @Query(value = "SELECT * "
      + "FROM TeamCalendarJpaEntity tc "
      + "JOIN FETCH tc.participants participants "
      + "WHERE participants.memberId = :memberId", nativeQuery = true)
  List<TeamCalendarJpaEntity> findByMemberId(Long memberId);

  @Query(value = "SELECT * "
      + "FROM TeamCalendarJpaEntity tc "
      + "WHERE tc.team_id = :teamId", nativeQuery = true)
  List<TeamCalendarJpaEntity> findByTeamId(Long teamId);

  @Query(value = "SELECT * " +
      "FROM TeamCalendarJpaEntity tc " +
      "WHERE tc.team_id = :teamId " +
      "AND (tc.start_date LIKE :yearMonth " +
      "OR tc.end_date LIKE :yearMonth) ", nativeQuery = true)
  List<TeamCalendarJpaEntity> findByIdAndYearMonth(Long teamId, String yearMonth);

}