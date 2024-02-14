package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.member.domain.Member;

public interface AddMemberPort {
    Team addMember(Member member);
}
