package idorm.idormServer.calendar.repository;

import idorm.idormServer.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    Optional<Calendar> findByIdAndIsDeletedIsFalse(Long id);

    @Query(value = "SELECT * " +
            "FROM calendar c " +
            "WHERE c.start_date LIKE :yearMonth " +
            "AND c.is_deleted = 0", nativeQuery = true)
    List<Calendar>  findByIsDeletedIsFalseAndStartDateLike(String yearMonth);
}
