package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.entity.SleepoverCalendar;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SleepoverCalendarRepository
	extends JpaRepository<SleepoverCalendar, Long>, SleepoverCalendarCustomRepository {

	List<SleepoverCalendar> findByMemberId(Long memberId);

	List<SleepoverCalendar> findByTeamId(Long teamId);
}