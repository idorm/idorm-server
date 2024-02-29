package idorm.idormServer.calendar.entity;

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

import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SleepoverCalendar {

	@Id
	@Column(name = "sleepover_calendar_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Period period;

	private Long memberId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	public SleepoverCalendar(final Period period, final Long memberId, final Team team) {
		validateConstructor(period, memberId, team);
		this.period = period;
		this.memberId = memberId;
		this.team = team;
	}

	public void update(Period period) {
		validateUniqueDate(period);
		this.period.updateSleepover(period);
	}

	private void validateConstructor(Period period, Long memberId, Team team) {
		Validator.validateNotNull(List.of(period, memberId, team));
	}

	private void validateUniqueDate(Period newPeriod) {
		this.period.validateUniqueDate(newPeriod);
	}

}