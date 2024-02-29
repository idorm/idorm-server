package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.entity.SleepoverCalendar;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SleepoverCalendarRepository extends JpaRepository<SleepoverCalendar, Long> {


  List<SleepoverCalendar> findByMemberId(Long memberId);

  List<SleepoverCalendar> findByTeamId(Long teamId);

}