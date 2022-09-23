package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.Calendar;
import idorm.idormServer.calendar.dto.DateFilterDto;
import idorm.idormServer.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    @Transactional
    public Calendar save(Calendar entity) {
        return calendarRepository.save(entity);
    }

    public Calendar find(Long id) {
        return calendarRepository.findById(id).orElseThrow();
    }

    public Page<Calendar> searchList(Pageable pageable, DateFilterDto dateFilterDto) {
        LocalDateTime startDateTime = dateFilterDto.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = dateFilterDto.getEndDate().plusDays(1).atStartOfDay();

        return calendarRepository.search(pageable, startDateTime, endDateTime);
    }

    @Transactional
    public Calendar update(Calendar entity) {
        return calendarRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        calendarRepository.deleteById(id);
    }
}
