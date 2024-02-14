package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.DeleteTeamPort;
import idorm.idormServer.calendar.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteTeamAdapter implements DeleteTeamPort {

    private final TeamRepository teamRepository;

    @Override
    public void deleteTeam(Team team) {
        teamRepository.deleteById(team.getId());
    }
}
