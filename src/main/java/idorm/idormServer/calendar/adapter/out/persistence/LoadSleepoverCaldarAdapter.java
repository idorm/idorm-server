package idorm.idormServer.calendar.adapter.out.persistence;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.adapter.out.exception.DuplicatedSleepoverDateException;
import idorm.idormServer.calendar.adapter.out.exception.NotFoundSleepoverCalendarException;
import idorm.idormServer.calendar.application.port.out.LoadSleepoverCalendarPort;
import idorm.idormServer.calendar.entity.Period;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import idorm.idormServer.calendar.entity.Team;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadSleepoverCaldarAdapter implements LoadSleepoverCalendarPort {

	private final SleepoverCalendarRepository sleepoverCalendarRepository;

	@Override
	public SleepoverCalendar findById(Long sleepoverCalendarId) {
		SleepoverCalendar response = sleepoverCalendarRepository.findById(sleepoverCalendarId)
			.orElseThrow(NotFoundSleepoverCalendarException::new);
		return response;
	}

	@Override
	public SleepoverCalendar findByIdAndMemberId(Long sleepoverCalendarId, Long memberId) {
		SleepoverCalendar response = sleepoverCalendarRepository.findByIdAndMemberId(sleepoverCalendarId, memberId);
		if (response == null) {
			throw new NotFoundSleepoverCalendarException();
		}
		return response;
	}

	@Override
	public List<SleepoverCalendar> findByToday(Team teamDomain) {
		List<SleepoverCalendar> responses = sleepoverCalendarRepository.findByToday(teamDomain);
		return responses.isEmpty() ? new ArrayList<>() : responses;
	}

	@Override
	public List<SleepoverCalendar> findByYearMonth(Team teamDomain, YearMonth yearMonth) {
		List<SleepoverCalendar> responses = sleepoverCalendarRepository.findByYearMonth(teamDomain, yearMonth);
		return responses.isEmpty() ? new ArrayList<>() : responses;
	}

	@Override
	public List<SleepoverCalendar> findByMemberId(Long memberId) {
		List<SleepoverCalendar> responses = sleepoverCalendarRepository.findByMemberId(
			memberId);
		return responses.isEmpty() ? new ArrayList<>() : responses;
	}

	@Override
	public void hasOverlappingDatesWithSleepoverId(Long memberId, Period period, Long sleepoverCalendarId) {
		Long count = sleepoverCalendarRepository.hasOverlappingDatesWithSleepoverId(memberId, period,
			sleepoverCalendarId);
		if (count > 0) {
			throw new DuplicatedSleepoverDateException();
		}
	}

	@Override
	public void hasOverlappingDatesWithoutSleepoverId(Long memberId, Period period) {
		Long count = sleepoverCalendarRepository.hasOverlappingDatesWithoutSleepoverId(memberId, period);
		if (count > 0) {
			throw new DuplicatedSleepoverDateException();
		}
	}
}
