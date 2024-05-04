package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.entity.OfficialCalendar;
import idorm.idormServer.matchingInfo.entity.DormCategory;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface OfficialCalendarCustomRepository {

	List<OfficialCalendar> findByToday(DormCategory dormCategory);
}