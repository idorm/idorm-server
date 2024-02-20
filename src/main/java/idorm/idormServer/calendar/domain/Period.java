package idorm.idormServer.calendar.domain;

import idorm.idormServer.calendar.adapter.out.exception.DuplicatedSleepoverDateException;
import idorm.idormServer.calendar.adapter.out.exception.IllegalArgumentDateSetException;
import idorm.idormServer.common.util.Validator;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class Period {

  private LocalDate startDate;
  private LocalDate endDate;

  public Period(final LocalDate startDate, final LocalDate endDate) {
    validate(startDate, endDate);
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public static Period forMapper(final LocalDate startDate, final LocalDate endDate) {
    return new Period(startDate, endDate);
  }

  void update(LocalDate startDate, LocalDate endDate) {
    validate(startDate, endDate);
    this.startDate = startDate;
    this.endDate = endDate;
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

  void validateUniqueDate(Period otherPeriod) {
    if (startDate.isBefore(otherPeriod.endDate) && endDate.isAfter(otherPeriod.startDate)) {
      throw new DuplicatedSleepoverDateException();
    }
  }

  boolean isSameDate() {
    return startDate.equals(endDate);
  }
}