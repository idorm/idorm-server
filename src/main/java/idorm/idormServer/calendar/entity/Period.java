package idorm.idormServer.calendar.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import idorm.idormServer.calendar.adapter.out.exception.DuplicatedSleepoverDateException;
import idorm.idormServer.calendar.adapter.out.exception.IllegalArgumentDateSetException;
import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	public Period(final LocalDate startDate, final LocalDate endDate) {
		validate(startDate, endDate);
		this.startDate = startDate;
		this.endDate = endDate;
	}

	void updateTeamCalendar(Period period) {
		validate(period.getStartDate(), period.getEndDate());
		this.startDate = period.getStartDate();
		this.endDate = period.getEndDate();
	}

	void updateSleepover(Period period) {
		validate(period.getStartDate(), period.getEndDate());
		validateUniqueDate(period);
		this.startDate = period.getStartDate();
		this.endDate = period.getEndDate();
	}

	boolean isSameDate() {
		return startDate.equals(endDate);
	}

	void validateUniqueDate(Period otherPeriod) {
		if (startDate.isBefore(otherPeriod.endDate) && endDate.isAfter(otherPeriod.startDate)) {
			throw new DuplicatedSleepoverDateException();
		}
	}

	void validate(LocalDate startDate, LocalDate endDate) {
		Validator.validateNotNull(List.of(startDate, endDate));
		validateValidDate(startDate, endDate);
	}

	private void validateValidDate(LocalDate startDate, LocalDate endDate) {
		if (endDate.isBefore(startDate)) {
			throw new IllegalArgumentDateSetException();
		}
	}
}