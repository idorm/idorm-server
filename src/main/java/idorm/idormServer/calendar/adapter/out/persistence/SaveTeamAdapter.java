package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.SaveTeamPort;
import idorm.idormServer.calendar.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveTeamAdapter implements SaveTeamPort {

    private final TeamRepository teamRepository;

    @Override
    public void save(Team team) {
        teamRepository.save(team);
    }
}