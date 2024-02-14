package idorm.idormServer.calendar.application.port.in;

import idorm.idormServer.calendar.application.port.in.dto.RoomMateCalendarSaveRequest;
import idorm.idormServer.calendar.application.port.in.dto.RoomMateCalendarUpdateRequest;
import idorm.idormServer.member.domain.Member;
import java.time.YearMonth;

public interface TeamCalendarUseCase {

    void save(Member member, RoomMateCalendarSaveRequest request);

    void findById(Long teamCalendarId);
    void findAllByMonth(YearMonth month);
    void update(RoomMateCalendarUpdateRequest request);
    void delete(Long teamCalendarId);
}
