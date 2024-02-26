package idorm.idormServer.calendar.entity;

import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import idorm.idormServer.calendar.adapter.out.exception.IllegalArgumentDateSetException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Duration {

  @Column(nullable = false)
  private LocalTime startTime;

  @Column(nullable = false)
  private LocalTime endTime;

  public static Duration of(Period period, LocalTime start, LocalTime end) {
    validate(period, start, end);
    return new Duration(start, end);
  }


  void update(Period period, LocalTime startTime, LocalTime endTime) {
    validate(period, startTime, endTime);
    this.startTime = startTime;
    this.endTime = endTime;
  }

  private static void validate(Period period, LocalTime startTime, LocalTime endTime) {
    if (Objects.isNull(startTime) || Objects.isNull(endTime)) {
      return;
    }

    if (period.isSameDate()) {
      validateValidDateTime(startTime, endTime);
    }
  }

  public static void validateValidDateTime(LocalTime startTime, LocalTime endTime) {
    if (endTime.isBefore(startTime)) {
     throw new IllegalArgumentDateSetException();
    }
  }

}