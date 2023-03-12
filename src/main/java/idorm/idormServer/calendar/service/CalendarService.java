package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.Calendar;
import idorm.idormServer.calendar.dto.DateFilterDto;
import idorm.idormServer.calendar.repository.CalendarRepository;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.photo.service.PhotoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final PhotoService photoService;

    @Transactional
    public Calendar save(Calendar entity) {
        return calendarRepository.save(entity);
    }

    public Calendar find(Long id) {
        try {
            return calendarRepository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new CustomException(null, CALENDAR_NOT_FOUND);
        }
    }

    public Page<Calendar> searchList(Pageable pageable, DateFilterDto dateFilterDto) {

        return calendarRepository.search(pageable, dateFilterDto._getStartDateTime(), dateFilterDto._getEndDateTime());
    }

    @Transactional
    public Calendar update(Calendar entity) {
        Long id = entity.getCalendarId();

        try {
            calendarRepository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new CustomException(null, CALENDAR_NOT_FOUND);
        }

        return calendarRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {

        Optional<Calendar> calendar = calendarRepository.findById(id);
        calendar.ifPresent( value -> {
            String imageUrl = value.getImageUrl();
            if(imageUrl != null) {
                String uuid = value.getImageUrl().split("/")[4];
                photoService.deleteImage(uuid);
            }
        });

        calendarRepository.deleteById(id);
    }
}
