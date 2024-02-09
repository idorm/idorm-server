package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import idorm.idormServer.calendar.dto.OfficialCalendarUpdateRequest;
import idorm.idormServer.calendar.repository.OfficialCalendarRepository;
import idorm.idormServer.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static idorm.idormServer.common.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OfficialCalendarService {

    private final OfficialCalendarRepository calendarRepository;

    /**
     * DB에 일정 저장 | 크롤링 시 사용 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public OfficialCalendar save(String inuPostId,
                                 String title,
                                 LocalDate inuPostCreatedAt,
                                 String postUrl) {
        try {

            OfficialCalendar officialCalendar = OfficialCalendar.builder()
                    .inuPostId(inuPostId)
                    .title(title)
                    .inuPostCreatedAt(inuPostCreatedAt)
                    .postUrl(postUrl)
                    .build();

            return calendarRepository.save(officialCalendar);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 일정 수정 | 관리자 사용 |
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
                .orElseThrow(() -> new CustomException(null, CALENDAR_NOT_FOUND));
    }

    /**
     * 일정 다건 조회 | 관리자 용 |
     * 500(SERVER_ERROR)
     */
    public List<OfficialCalendar> findManyByAdmin() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            LocalDate now = LocalDate.now();
            LocalDate lastWeek = now.minusDays(7);
            return calendarRepository.findByMonthByAdmin(now.format(formatter) + "-%",
                    lastWeek.format(formatter) + "-%");
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 일정 다건 조회 | 회원 용 |
     * 500(SERVER_ERROR)
     */
    public List<OfficialCalendar> findManyByYearMonth(YearMonth yearMonth) {

        try {
            return calendarRepository.findByMonthByMember(yearMonth + "-%");
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 기숙사 별 오늘의 일정 조회 |
     * 500(SERVER_ERROR)
     */
    public List<OfficialCalendar> findTodayCalendars(int dormNum) {

        try {
            switch (dormNum) {
                case 1:
                    return calendarRepository.findCalendarsByDorm1AndTodayStartDate();
                case 2:
                    return calendarRepository.findCalendarsByDorm2AndTodayStartDate();
                default:
                    return calendarRepository.findCalendarsByDorm3AndTodayStartDate();
            }
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
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

    /**
     * 크롤링한 공식 일정 저장 여부 확인 |
     * 500(SERVER_ERROR)
     */
    public boolean validateOfficialCalendarExistence(String inuPostId) {
        if (inuPostId == null)
            return false;
        return calendarRepository.existsByInuPostIdAndIsDeletedIsFalse(inuPostId);
    }
}
