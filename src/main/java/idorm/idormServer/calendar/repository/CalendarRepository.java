package idorm.idormServer.calendar.repository;

import idorm.idormServer.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    Optional<Calendar> findByIdAndIsDeletedIsFalse(Long id);

    @Query(value = "SELECT * " +
            "FROM calendar c " +
            "WHERE c.start_date LIKE :yearMonth " +
            "AND c.is_deleted = 0", nativeQuery = true)
    List<Calendar> findByIsDeletedIsFalseAndStartDateLike(String yearMonth);

    @Query(value = "SELECT * " +
            "FROM calendar c " +
            "WHERE c.is_dorm1yn = 1 " +
            "AND c.start_date = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 9 HOUR), '%Y-%m-%d') " +
            "AND c.is_deleted = 0", nativeQuery = true)
    List<Calendar> findCalendarsByDorm1AndTodayStartDate();

    @Query(value = "SELECT * " +
            "FROM calendar c " +
            "WHERE c.is_dorm2yn = 1 " +
            "AND c.start_date = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 9 HOUR), '%Y-%m-%d') " +
            "AND c.is_deleted = 0", nativeQuery = true)
    List<Calendar> findCalendarsByDorm2AndTodayStartDate();

    @Query(value = "SELECT * " +
            "FROM calendar c " +
            "WHERE c.is_dorm3yn = 1 " +
            "AND c.start_date = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 9 HOUR), '%Y-%m-%d') " +
            "AND c.is_deleted = 0", nativeQuery = true)
    List<Calendar> findCalendarsByDorm3AndTodayStartDate();
}

