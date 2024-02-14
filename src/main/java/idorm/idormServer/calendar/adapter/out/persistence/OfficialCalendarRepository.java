package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OfficialCalendarRepository extends JpaRepository<OfficialCalendarJpaEntity, Long> {

    Optional<OfficialCalendar> findByIdAndIsDeletedIsFalse(Long id);

    boolean existsByInuPostIdAndIsDeletedIsFalse(String inuPostId);

    @Query(value = "SELECT * " +
            "FROM official_calendar c " +
            "WHERE (c.inu_post_created_at LIKE :now " +
            "OR c.inu_post_created_at LIKE :lastWeek) " +
            "AND c.is_deleted = 0", nativeQuery = true)
    List<OfficialCalendar> findByMonthByAdmin(String now, String lastWeek);

    @Query(value = "SELECT * " +
            "FROM official_calendar c " +
            "WHERE (c.start_date LIKE :yearMonth " +
            "OR c.end_date LIKE :yearMonth) " +
            "AND c.is_deleted = 0 " +
            "AND c.is_public = 1", nativeQuery = true)
    List<OfficialCalendar> findByMonthByMember(@Param("yearMonth") String yearMonth);

    @Query(value = "SELECT * " +
            "FROM official_calendar c " +
            "WHERE c.is_dorm1yn = 1 " +
            "AND c.start_date = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 9 HOUR), '%Y-%m-%d') " +
            "AND c.is_deleted = 0 " +
            "AND c.is_public = 1", nativeQuery = true)
    List<OfficialCalendar> findCalendarsByDorm1AndTodayStartDate();

    @Query(value = "SELECT * " +
            "FROM official_calendar c " +
            "WHERE c.is_dorm2yn = 1 " +
            "AND c.start_date = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 9 HOUR), '%Y-%m-%d') " +
            "AND c.is_deleted = 0 " +
            "AND c.is_public = 1", nativeQuery = true)
    List<OfficialCalendar> findCalendarsByDorm2AndTodayStartDate();

    @Query(value = "SELECT * " +
            "FROM official_calendar c " +
            "WHERE c.is_dorm3yn = 1 " +
            "AND c.start_date = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 9 HOUR), '%Y-%m-%d') " +
            "AND c.is_deleted = 0 " +
            "AND c.is_public = 1", nativeQuery = true)
    List<OfficialCalendar> findCalendarsByDorm3AndTodayStartDate();
}

