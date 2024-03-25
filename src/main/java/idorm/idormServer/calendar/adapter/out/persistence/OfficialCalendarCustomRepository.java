package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import idorm.idormServer.calendar.entity.OfficialCalendar;
import idorm.idormServer.matchingInfo.entity.DormCategory;

@Repository
public interface OfficialCalendarCustomRepository {

	List<OfficialCalendar> findByToday(DormCategory dormCategory);
}