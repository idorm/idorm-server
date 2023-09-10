package idorm.idormServer.calendar.repository;

import idorm.idormServer.calendar.domain.CrawledOfficialCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawledOfficialCalendarRepository extends JpaRepository<CrawledOfficialCalendar, Long> {

    boolean existsByInuPostIdAndIsDeletedIsFalse(String inuPostId);
}
