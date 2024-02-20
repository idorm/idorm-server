package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.adapter.out.exception.NotFoundTeamException;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.domain.Team;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadTeamAdapter implements LoadTeamPort {

	private final TeamMapper teamMapper;
	private final TeamRepository teamRepository;

	@Override
	public Optional<Team> findByMemberIdWithOptional(Long memberId) {
		Optional<TeamJpaEntity> response = teamRepository.findByMemberIdWithOptional(memberId);
		return Optional.ofNullable(response.map(teamMapper::toDomain).orElse(null));
	}

	@Override
	public Team findByMemberId(Long memberId) {
		TeamJpaEntity response = teamRepository.findByMemberIdWithCalendarsAndMembers(memberId)
			.orElseThrow(NotFoundTeamException::new);
		return teamMapper.toDomain(response);
	}

	@Override
	public Team findByMemberIdWithTeamMember(Long memberId) {
		TeamJpaEntity response = teamRepository.findByMemberIdWithTeamMember(memberId)
			.orElseThrow(NotFoundTeamException::new);
		return teamMapper.toDomain(response);
	}
}
