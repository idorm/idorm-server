package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.Calendar;
import idorm.idormServer.calendar.repository.CalendarRepository;
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
public class CalendarService {

    private final CalendarRepository calendarRepository;

    /**
     * DB에 일정 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Calendar save(Calendar calendar) {
        try {
            return calendarRepository.save(calendar);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 일정 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(Calendar calendar) {
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
    public Calendar findOneById(Long id) {
        return calendarRepository.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> {
                    throw new CustomException(null, CALENDAR_NOT_FOUND);
                });
    }

    /**
     * 일정 다건 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Calendar> findManyByYearMonth(YearMonth yearMonth) {

        try {
            return calendarRepository.findByIsDeletedIsFalseAndStartDateLike(yearMonth + "-%");
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 1 기숙사 오늘의 일정 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Calendar> findTodayCalendarsFromDorm1() {
        return calendarRepository.findCalendarsByDorm1AndTodayStartDate();
    }

    /**
     * 2 기숙사 오늘의 일정 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Calendar> findTodayCalendarsFromDorm2() {
        return calendarRepository.findCalendarsByDorm2AndTodayStartDate();
    }

    /**
     * 3 기숙사 오늘의 일정 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Calendar> findTodayCalendarsFromDorm3() {
        return calendarRepository.findCalendarsByDorm3AndTodayStartDate();
    }

    /**
     * 일정 시작 및 종료 일자 검증 |
     * 400(DATE_SET_INVALID)
     */

    public void validateStartAndEndDate(LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null)
            return;

        if (startDate.isAfter(endDate))
            throw new CustomException(null, DATE_SET_INVALID);
    }
}
