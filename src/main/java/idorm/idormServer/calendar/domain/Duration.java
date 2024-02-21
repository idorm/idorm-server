package idorm.idormServer.calendar.domain;

import idorm.idormServer.calendar.adapter.out.exception.IllegalArgumentDateSetException;
import java.time.LocalTime;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Duration {

  private LocalTime startTime;
  private LocalTime endTime;

  public Duration(final LocalTime startTime, final LocalTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public static Duration of(Period period, LocalTime start, LocalTime end) {
    validate(period, start, end);
    return new Duration(start, end);
  }

  public static Duration forMapper(final LocalTime startTime, final LocalTime endTime) {
    return new Duration(startTime, endTime);
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