package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.adapter.out.exception.NotFoundCalendarException;
import idorm.idormServer.calendar.application.port.out.LoadOfficialCalendarPort;
import idorm.idormServer.calendar.entity.OfficialCalendar;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadOfficialCalendarAdapter implements LoadOfficialCalendarPort {

    private final OfficialCalendarRepository officialCalendarRepository;

    @Override
    public OfficialCalendar findById(Long officialCalendarId) {
        OfficialCalendar response = officialCalendarRepository.findById(officialCalendarId)
            .orElseThrow(NotFoundCalendarException::new);
        return response;
    }

    @Override
    public List<OfficialCalendar> findByToday(DormCategory dormCategory) {
        List<OfficialCalendar> responses = officialCalendarRepository.findByToday(dormCategory);
        return responses != null ? responses : Collections.emptyList();
    }

    @Override
    public List<OfficialCalendar> findByMonthByAdmin(String now, String lastWeek) {
        List<OfficialCalendar> responses = officialCalendarRepository.findByMonthByAdmin(now, lastWeek);
        return responses != null ? responses : Collections.emptyList();
    }

    @Override
    public List<OfficialCalendar> findByMonthByMember(YearMonth yearMonth) {
        List<OfficialCalendar> responses = officialCalendarRepository.findByMonthByMember(yearMonth);
        return responses != null ? responses : Collections.emptyList();
    }

    @Override
    public Boolean findByInuPostId(String inuPostId) {
        return officialCalendarRepository.existsByInuPostIdAndIsDeletedIsFalse(inuPostId);
    }
}