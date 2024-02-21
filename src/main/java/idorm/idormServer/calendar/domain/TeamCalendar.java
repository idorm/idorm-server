package idorm.idormServer.calendar.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import idorm.idormServer.calendar.adapter.out.exception.AccessDeniedTeamCalendarException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamCalendar {

	private Long id;
	private Period period;
	private Duration duration;
	private Title title;
	private Content content;
	private Participants participants;
	private Team team;

	public TeamCalendar(
		final Title title,
		final Content content,
		final Period period,
		final Duration duration,
		final Participants participants,
		final Team team) {
		this.period = period;
		this.duration = duration;
		this.title = title;
		this.content = content;
		this.participants = participants;
		this.team = team;
	}

	public static TeamCalendar forMapper(final Long id,
		final Period period,
		final Duration duration,
		final Title title,
		final Content content,
		final Participants participants,
		final Team team) {
		return new TeamCalendar(id, period, duration, title, content, participants, team);
	}

	public void participate(Long memberId) {
		participants.participate(memberId);
	}

	public void update(Team team, String title, String content, Period period,
		LocalTime startTime, LocalTime endTime) {
		validateAuthorization(team);
		this.title.updateTeamCalendar(title);
		this.content.update(content);
		this.period.update(period);
		this.duration.update(period, startTime, endTime);
	}

	public void deleteParticipant(Long participant) {
		this.participants.delete(participant);
	}

	 public void validateAuthorization(final Team team) {
	 	if (!this.team.equals(team)) {
	 		throw new AccessDeniedTeamCalendarException();
	 	}
	 }
}