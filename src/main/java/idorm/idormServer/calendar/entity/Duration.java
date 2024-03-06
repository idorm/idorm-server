package idorm.idormServer.calendar.entity;

import idorm.idormServer.calendar.adapter.out.exception.IllegalArgumentDateSetException;
import idorm.idormServer.common.util.Validator;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Duration {

  @Column(nullable = false)
  private LocalTime startTime;

  @Column(nullable = false)
  private LocalTime endTime;

  public Duration(LocalTime startTime, LocalTime endTime) {
    validate(startTime, endTime);
    this.startTime = startTime;
    this.endTime = endTime;
  }


  void update(Period period, LocalTime startTime, LocalTime endTime) {
    isSameDate(period, startTime, endTime);
    validate(startTime, endTime);
    this.startTime = startTime;
    this.endTime = endTime;
  }

  private void validate(LocalTime startTime, LocalTime endTime) {
    Validator.validateNotNull(List.of(startTime, endTime));
    validateValidDateTime(startTime, endTime);
  }

  private void validateValidDateTime(LocalTime startTime, LocalTime endTime) {
    if (endTime.isBefore(startTime)) {
      throw new IllegalArgumentDateSetException();
    }
  }

  private void isSameDate(Period period, LocalTime startTime, LocalTime endTime) {
    if (period.isSameDate()) {
      validateValidDateTime(startTime, endTime);
    }
  }
}