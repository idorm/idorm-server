package idorm.idormServer.calendar.application.port.in;

import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarRequest;
import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarUpdateRequest;
import idorm.idormServer.member.domain.Member;
import java.time.YearMonth;

public interface SleepoverCalendarUseCase {
    void save(Member member, SleepoverCalendarRequest request);
    void findById(Member member, Long teamCalendarId);
    void findAllByMonth(Member member,YearMonth month);
    void update(Member member, SleepoverCalendarUpdateRequest request);
    void delete(Member member, Long teamCalendarId);
}
