package idorm.idormServer.calendar.domain;

import static idorm.idormServer.common.exception.ExceptionCode.ILLEGAL_ARGUMENT_DATE_SET;

import idorm.idormServer.common.exception.CustomException;
import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Duration {

    private LocalTime startTime;

    private LocalTime endTime;

    public Duration(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Duration of(Period period, LocalTime start, LocalTime end) {
        validate(period, start, end);
        return new Duration(start, end);
    }

    private static void validate(Period period, LocalTime startTime, LocalTime endTime) {
        if (Objects.isNull(startTime) || Objects.isNull(endTime)) {
            return;
        }

        if (period.isSameDate()) {
            validateValidDateTime(startTime, endTime);
        }
    }

    private static void validateValidDateTime(LocalTime startTime, LocalTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new CustomException(null, ILLEGAL_ARGUMENT_DATE_SET);
        }
    }
}