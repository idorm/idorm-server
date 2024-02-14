package idorm.idormServer.calendar.domain;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.common.util.Validator;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

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

    private void validate(LocalDate startDate, LocalDate endDate) {
        Validator.validateNotNull(List.of(startDate, endDate));
        validateValidDate(startDate, endDate);
    }

    public void validateValidDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new CustomException(null, ExceptionCode.ILLEGAL_ARGUMENT_DATE_SET);
        }
    }

    void validateUniqueDate(Period otherPeriod) {
        if (startDate.isBefore(otherPeriod.endDate) && endDate.isAfter(otherPeriod.startDate)) {
            throw new CustomException(null, ExceptionCode.ILLEGAL_ARGUMENT_DATE_SET);
        }
    }

    boolean isSameDate() {
        return startDate.equals(endDate);
    }
}