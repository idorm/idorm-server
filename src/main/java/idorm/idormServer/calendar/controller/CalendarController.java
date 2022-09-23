package idorm.idormServer.calendar.controller;

import idorm.idormServer.calendar.domain.Calendar;
import idorm.idormServer.calendar.dto.CalendarRequest;
import idorm.idormServer.calendar.service.CalendarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("calendar")
@RequiredArgsConstructor
@Api(tags = "05. 캘린더")
public class CalendarController {
    // TODO : Authentication 추가

    private final CalendarService calendarService;

    @PostMapping("")
    @ApiOperation("일정 저장")
    Calendar save(@RequestBody CalendarRequest request) {
        return calendarService.save(request.toEntity(null));
    }

    @GetMapping("{id}")
    @ApiOperation("일정 단건 조회")
    Calendar find(@PathVariable Long id) {
        return calendarService.find(id);
    }

    @GetMapping("list")
    @ApiOperation("일정 목록 조회")
    List<Calendar> searchList() {
        return calendarService.searchList();
    }

    @PutMapping("{id}")
    @ApiOperation("일정 단건 수정")
    Calendar update(@PathVariable Long id, @RequestBody CalendarRequest request) {
        return calendarService.update(request.toEntity(id));
    }

    @DeleteMapping("{id}")
    @ApiOperation("일정 단건 삭제")
    void delete(@PathVariable Long id) {
        calendarService.delete(id);
    }
}
