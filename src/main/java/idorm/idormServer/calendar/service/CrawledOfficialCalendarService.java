package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.CrawledOfficialCalendar;
import idorm.idormServer.calendar.repository.CrawledOfficialCalendarRepository;
import idorm.idormServer.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrawledOfficialCalendarService {

    private final CrawledOfficialCalendarRepository officialCrawledRepository;

    /**
     * 크롤링한 공식 일정 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public CrawledOfficialCalendar save(String title, String websiteUrl, String inuPostId) {
        try {
            CrawledOfficialCalendar officialCrawledCalendar = CrawledOfficialCalendar.builder()
                    .title(title)
                    .websiteUrl(websiteUrl)
                    .inuPostId(inuPostId)
                    .build();

            return officialCrawledRepository.save(officialCrawledCalendar);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 크롤링한 공식 일정 저장 여부 확인 |
     * 500(SERVER_ERROR)
     */
    public boolean validateCrawledCalendarExistence(String inuPostId) {
        try {
            return officialCrawledRepository.existsByInuPostIdAndIsDeletedIsFalse(inuPostId);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
}
