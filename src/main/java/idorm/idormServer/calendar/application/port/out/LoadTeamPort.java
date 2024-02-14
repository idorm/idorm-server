package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.member.domain.Member;

public interface LoadTeamPort {

    Team load(Member member);
}
