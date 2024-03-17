package idorm.idormServer.calendar.adapter.out.persistence;

import java.time.YearMonth;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import idorm.idormServer.calendar.entity.OfficialCalendar;

@Repository
public interface OfficialCalendarRepository
	extends JpaRepository<OfficialCalendar, Long>, OfficialCalendarCustomRepository {

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
	List<OfficialCalendar> findByMonthByMember(@Param("yearMonth") YearMonth yearMonth);

}

