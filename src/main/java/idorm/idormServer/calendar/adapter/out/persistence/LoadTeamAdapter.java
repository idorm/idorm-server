package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.adapter.out.exception.NotFoundTeamException;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.entity.Team;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadTeamAdapter implements LoadTeamPort {

	private final TeamRepository teamRepository;

	@Override
	public Optional<Team> findByMemberIdWithOptional(Long memberId) {
		Team response = teamRepository.findByMemberId(memberId);
		return Optional.ofNullable(response);
	}

	@Override
	public Team findByMemberId(Long memberId) {
		Team response = findByMemberId(memberId);

		if (response == null) {
			throw new NotFoundTeamException();
		}
		return response;
	}
}
