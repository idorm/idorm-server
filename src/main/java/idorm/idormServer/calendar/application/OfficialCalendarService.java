package idorm.idormServer.calendar.application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.OfficialCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.CrawledOfficialCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.FindOfficialCalendarsRequest;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarUpdateRequest;
import idorm.idormServer.calendar.application.port.out.LoadOfficialCalendarPort;
import idorm.idormServer.calendar.entity.OfficialCalendar;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OfficialCalendarService implements OfficialCalendarUseCase {

	private final LoadOfficialCalendarPort loadOfficialCalendarPort;

	@Override
	public OfficialCalendarResponse update(OfficialCalendarUpdateRequest request) {
		OfficialCalendar officialCalendar = loadOfficialCalendarPort.findById(request.calendarId());

		officialCalendar.update(request.isDorm1Yn(), request.isDorm2Yn(), request.isDorm3Yn(), request.startDate(),
			request.endDate(), request.title());
		return OfficialCalendarResponse.from(officialCalendar);
	}

	@Override
	public void delete(Long officialCalendarId) {
		OfficialCalendar officialCalendar = loadOfficialCalendarPort.findById(officialCalendarId);
		officialCalendar.delete();
	}

	@Override
	public List<CrawledOfficialCalendarResponse> findByMonthByAdmin() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		LocalDate now = LocalDate.now();
		LocalDate lastWeek = now.minusDays(7);
		List<OfficialCalendar> officialCalendars = loadOfficialCalendarPort.findByMonthByAdmin(
			now.format(formatter) + "-%",
			lastWeek.format(formatter) + "-%");
		List<CrawledOfficialCalendarResponse> responses = officialCalendars.stream()
			.map(CrawledOfficialCalendarResponse::from)
			.toList();
		return responses;
	}

	@Override
	public OfficialCalendarResponse findOneByAdmin(Long officialCalendarId) {
		final OfficialCalendar officialCalendar = loadOfficialCalendarPort.findById(officialCalendarId);
		return OfficialCalendarResponse.from(officialCalendar);
	}

	@Override
	public List<OfficialCalendarResponse> findByMonthByMember(AuthResponse authResponse,
		FindOfficialCalendarsRequest request) {
		final List<OfficialCalendar> officialCalendars = loadOfficialCalendarPort.findByMonthByMember(
			request.yearMonth());
		List<OfficialCalendarResponse> responses = officialCalendars.stream()
			.map(OfficialCalendarResponse::from)
			.toList();
		return responses;
	}
}


/*	public List<OfficialCalendar> findTodayCalendars(int dormNum) {

		try {
			switch (dormNum) {
				case 1:
					return calendarRepository.findCalendarsByDorm1AndTodayStartDate();
				case 2:
					return calendarRepository.findCalendarsByDorm2AndTodayStartDate();
				default:
					return calendarRepository.findCalendarsByDorm3AndTodayStartDate();
			}
		}
		public void validateStartAndEndDate(LocalDate startDate, LocalDate endDate) {

			if (startDate == null || endDate == null)
				throw new CustomException(null, DATE_FIELD_REQUIRED);

			if (startDate.isAfter(endDate))
				throw new CustomException(null, ILLEGAL_ARGUMENT_DATE_SET);
		}

}*/
