package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.SaveTeamPort;
import idorm.idormServer.calendar.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveTeamAdapter implements SaveTeamPort {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    public void saveTeam(Team team) {
        TeamJpaEntity teamJpaEntity = teamMapper.toEntity(team);
        teamRepository.save(teamJpaEntity);
    }
}