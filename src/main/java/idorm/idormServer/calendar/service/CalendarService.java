package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.Calendar;
import idorm.idormServer.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    public Calendar save(Calendar entity) {
        return calendarRepository.save(entity);
    }

    public Calendar find(Long id) {
        return calendarRepository.findById(id).orElseThrow();
    }

    public List<Calendar> searchList() {
        return calendarRepository.findAll();
    }

    public Calendar update(Calendar entity) {
        return calendarRepository.save(entity);
    }

    public void delete(Long id) {
        calendarRepository.deleteById(id);
    }
}
