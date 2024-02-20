package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.domain.Team;

public interface SaveTeamPort {
    void save(Team team);
}
