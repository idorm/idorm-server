package idorm.idormServer.calendar.application.port.in;

import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarsFindRequest;
import idorm.idormServer.member.domain.Member;
import java.util.List;

public interface TeamUseCase {
    void findAllCalendars(Member member, OfficialCalendarsFindRequest request);
    void addTeamMember(Member member, Long registerMemberId);
    void deleteMember(Member member, Long memberId);
    List<Member> findTeamMembers(Member member);
    void isConfirmTeamExploded(Member member);
}
