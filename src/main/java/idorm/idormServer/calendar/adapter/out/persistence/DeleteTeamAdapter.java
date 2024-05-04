package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.DeleteTeamPort;
import idorm.idormServer.calendar.entity.Team;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteTeamAdapter implements DeleteTeamPort {

	private final TeamRepository teamRepository;

	@Override
	public void delete(Team team) {
		teamRepository.delete(team);
	}
}