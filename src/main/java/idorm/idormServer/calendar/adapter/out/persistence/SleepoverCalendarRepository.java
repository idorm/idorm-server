package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import idorm.idormServer.calendar.entity.SleepoverCalendar;

@Repository
public interface SleepoverCalendarRepository extends JpaRepository<SleepoverCalendar, Long> {

	List<SleepoverCalendar> findByMemberId(Long memberId);

	List<SleepoverCalendar> findByTeamId(Long teamId);
}