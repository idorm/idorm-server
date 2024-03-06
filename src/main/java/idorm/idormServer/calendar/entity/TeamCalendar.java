package idorm.idormServer.calendar.entity;

import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.*;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import idorm.idormServer.calendar.adapter.out.exception.AccessDeniedTeamCalendarException;
import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCalendar {

	private static final int MIN_LENGTH = 1;
	private static final int MAX_TITLE_LENGTH = 15;
	private static final int MAX_CONTENT_LENGTH = 100;

	@Id
	@Column(name = "team_calendar_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Period period;

	@Embedded
	private Duration duration;

	@Column(name = "title", nullable = false, length = MAX_TITLE_LENGTH)
	private String title;

	@Column(name = "content", nullable = false, length = MAX_CONTENT_LENGTH)
	private String content;

	@Embedded
	private Participants participants;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	public TeamCalendar(
		final String title,
		final String content,
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

	public void update(Team team, String title, String content, Period period, LocalTime startTime, LocalTime endTime,
		List<Long> ids) {
		validateAuthorization(team);
		validateConstructor(title, content);
		removePariticipants(ids);
		newParticipates(ids);
		this.title = title;
		this.content = content;
		this.period.updateTeamCalendar(period);
		this.duration.update(period, startTime, endTime);
	}

	public void newParticipates(List<Long> memberIds) {
		memberIds.stream()
			.filter(memberId -> !this.participants.contains(memberId))
			.forEach(this.participants::participate);
	}

	public void removePariticipants(List<Long> memberIds) {
		List<Long> existingMembers = this.participants.getParticipants().stream()
			.map(Participant::getMemberId)
			.distinct()
			.toList();

		existingMembers.stream()
			.filter(memberId -> !memberIds.contains(memberId))
			.forEach(this.participants::delete);
	}

	public List<Participant> getParticipants() {
		return participants.getParticipants();
	}

	public List<Long> getParticipantMemberIds() {
		return participants.getParticipants().stream()
			.map(Participant::getMemberId)
			.toList();
	}

	private void validateAuthorization(final Team team) {
		if (!this.team.equals(team)) {
			throw new AccessDeniedTeamCalendarException();
		}
	}

	private void validateConstructor(final String title, final String content) {
		validateTitle(title);
		validateContent(content);
	}

	private void validateTitle(final String title) {
		Validator.validateNotBlank(title);
		Validator.validateLength(title, MIN_LENGTH, MAX_TITLE_LENGTH, INVALID_TITLE_LENGTH);
	}

	private void validateContent(final String content) {
		Validator.validateNotBlank(content);
		Validator.validateLength(content, MIN_LENGTH, MAX_CONTENT_LENGTH, INVALID_CONTENT_LENGTH);
	}
}