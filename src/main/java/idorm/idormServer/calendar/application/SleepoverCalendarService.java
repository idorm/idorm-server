package idorm.idormServer.calendar.application;

import idorm.idormServer.calendar.application.port.in.SleepoverCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarRequest;
import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarUpdateRequest;
import idorm.idormServer.member.domain.Member;
import java.time.YearMonth;

public class SleepoverCalendarService implements SleepoverCalendarUseCase {


    @Override
    public void save(Member member, SleepoverCalendarRequest request) {

    }

    @Override
    public void findById(Member member, Long teamCalendarId) {

    }

    @Override
    public void findAllByMonth(Member member, YearMonth month) {

    }

    @Override
    public void update(Member member, SleepoverCalendarUpdateRequest request) {

    }

    @Override
    public void delete(Member member, Long teamCalendarId) {

    }
}
