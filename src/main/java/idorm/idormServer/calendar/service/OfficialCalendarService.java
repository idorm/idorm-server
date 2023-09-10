package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import idorm.idormServer.calendar.dto.OfficialCalendarUpdateRequest;
import idorm.idormServer.calendar.repository.OfficialCalendarRepository;
import idorm.idormServer.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OfficialCalendarService {

    private final OfficialCalendarRepository calendarRepository;

    /**
     * DB에 일정 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public OfficialCalendar save(OfficialCalendar calendar) {
        try {
            return calendarRepository.save(calendar);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 일정 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void update(OfficialCalendar officialCalendar, OfficialCalendarUpdateRequest request) {
        try {
            officialCalendar.update(request);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 일정 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(OfficialCalendar calendar) {
        try {
            calendar.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 일정 단건 조회 |
     * 404(CALENDAR_NOT_FOUND)
     */
    public OfficialCalendar findOneById(Long id) {
        return calendarRepository.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> {
                    throw new CustomException(null, CALENDAR_NOT_FOUND);
                });
    }

    /**
     * 일정 전체 조회 |
     * 500(SERVER_ERROR)
     */
    public List<OfficialCalendar> findMany() {
        try {
            return calendarRepository.findByIsDeletedIsFalse();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 일정 다건 조회 |
     * 500(SERVER_ERROR)
     */
    public List<OfficialCalendar> findManyByYearMonth(YearMonth yearMonth) {

        try {
            return calendarRepository.findByIsDeletedIsFalseAndDateLike(yearMonth + "-%");
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 1 기숙사 오늘의 일정 조회 |
     * 500(SERVER_ERROR)
     */
    public List<OfficialCalendar> findTodayCalendarsFromDorm1() {
        return calendarRepository.findCalendarsByDorm1AndTodayStartDate();
    }

    /**
     * 2 기숙사 오늘의 일정 조회 |
     * 500(SERVER_ERROR)
     */
    public List<OfficialCalendar> findTodayCalendarsFromDorm2() {
        return calendarRepository.findCalendarsByDorm2AndTodayStartDate();
    }

    /**
     * 3 기숙사 오늘의 일정 조회 |
     * 500(SERVER_ERROR)
     */
    public List<OfficialCalendar> findTodayCalendarsFromDorm3() {
        return calendarRepository.findCalendarsByDorm3AndTodayStartDate();
    }

    /**
     * 일정 시작 및 종료 일자 검증 |
     * 400(DATE_FIELD_REQUIRED)
     * 400(ILLEGAL_ARGUMENT_DATE_SET)
     */

    public void validateStartAndEndDate(LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null)
            throw new CustomException(null, DATE_FIELD_REQUIRED);

        if (startDate.isAfter(endDate))
            throw new CustomException(null, ILLEGAL_ARGUMENT_DATE_SET);
    }
}
