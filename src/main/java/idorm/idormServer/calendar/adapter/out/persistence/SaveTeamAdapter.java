package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.application.port.out.SaveTeamPort;
import idorm.idormServer.calendar.domain.Team;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveTeamAdapter implements SaveTeamPort {

	private final TeamRepository teamRepository;
	private final TeamMapper teamMapper;

	@Override
	public void save(Team team) {
		teamRepository.save(teamMapper.toEntity(team));
	}
}