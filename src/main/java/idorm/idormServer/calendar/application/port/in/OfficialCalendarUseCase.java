package idorm.idormServer.calendar.application.port.in;

import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarUpdateRequest;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarsFindRequest;
import idorm.idormServer.member.domain.Member;

public interface OfficialCalendarUseCase {

    void update(Member member, OfficialCalendarUpdateRequest request);
    void delete(Member member, Long officialCalendarId);
    void findAllByAdmin(Member member);
    void findOneByAdmin(Member member, Long officialCalendarId);
    void findAllByMember(Member member, OfficialCalendarsFindRequest request);

}
