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
		Optional<Team> response = teamRepository.findByMemberIdWithOptional(memberId);
		return Optional.ofNullable(response).orElse(null);
	}

	@Override
	public Team findByMemberId(Long memberId) {
		Team response = teamRepository.findByMemberIdWithCalendarsAndMembers(memberId)
			.orElseThrow(NotFoundTeamException::new);
		return response;
	}

	@Override
	public Team findByMemberIdWithTeamMember(Long memberId) {
		Team response = teamRepository.findByMemberIdWithTeamMember(memberId)
			.orElseThrow(NotFoundTeamException::new);
		return response;
	}
}
