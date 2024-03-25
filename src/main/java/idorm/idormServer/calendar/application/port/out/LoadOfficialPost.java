package idorm.idormServer.calendar.application.port.out;

import java.util.List;

import idorm.idormServer.calendar.entity.OfficialCalendar;

public interface LoadOfficialPost {
	List<OfficialCalendar> findNewPosts();
}
