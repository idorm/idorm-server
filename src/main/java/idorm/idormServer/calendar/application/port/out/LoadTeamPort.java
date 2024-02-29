package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.entity.Team;
import java.util.Optional;

public interface LoadTeamPort {

  Optional<Team> findByMemberIdWithOptional(Long memberId);

  Team findByMemberId(Long memberid);
}
