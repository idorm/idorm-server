package idorm.idormServer.calendar.application.port.out;

import java.util.Optional;

import idorm.idormServer.calendar.entity.Team;

public interface LoadTeamPort {

	Optional<Team> findByMemberIdWithOptional(Long memberId);

	Team findByMemberIdWithTeamMember(Long memberId);

	Team findByMemberId(Long memberid);
}
