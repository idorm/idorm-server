package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.entity.Team;

public interface DeleteTeamPort {

	void delete(Team team);
}