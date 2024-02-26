package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.application.port.out.DeleteTeamPort;
import idorm.idormServer.calendar.entity.Team;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteTeamAdapter implements DeleteTeamPort {

    private final TeamRepository teamRepository;

    @Override
    public void deleteTeam(Team team) {
        teamRepository.delete(team);
    }
}
