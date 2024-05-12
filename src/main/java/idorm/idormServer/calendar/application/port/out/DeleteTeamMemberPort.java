package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.member.entity.Member;

public interface DeleteTeamMemberPort {
	void deleteTeamMember(final Member member);
}
