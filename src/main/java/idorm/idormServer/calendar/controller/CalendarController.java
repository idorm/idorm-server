package idorm.idormServer.calendar.controller;

import idorm.idormServer.calendar.service.CalendarService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("calendar")
@RequiredArgsConstructor
@Api(tags = "05. 캘린더")
public class CalendarController {
    private final CalendarService calendarService;
}
