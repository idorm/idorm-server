package idorm.idormServer.calendar.application.port.in.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import idorm.idormServer.calendar.entity.OfficialCalendar;

public record CrawledOfficialCalendarResponse(
	Long officialCalendarId,
	String inuPostId,
	String title,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate inuPostCreatedAt,
	String postUrl,
	Boolean isPublic
) {

	public static CrawledOfficialCalendarResponse from(final OfficialCalendar officialCalendar) {
		return new CrawledOfficialCalendarResponse(officialCalendar.getId(),
			officialCalendar.getInuPostId(),
			officialCalendar.getTitle(),
			officialCalendar.getInuPostCreatedAt(),
			officialCalendar.getInuPostUrl(),
			officialCalendar.getIsPublic());
	}
}
