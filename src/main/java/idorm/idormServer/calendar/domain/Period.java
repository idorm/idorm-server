package idorm.idormServer.calendar.domain;

import static idorm.idormServer.common.exception.ExceptionCode.ILLEGAL_ARGUMENT_DATE_SET;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.util.Validator;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;

    public Period(LocalDate startDate, LocalDate endDate) {
        validate(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(LocalDate startDate, LocalDate endDate) {
        validate(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validate(LocalDate startDate, LocalDate endDate) {
        Validator.validateNotNull(List.of(startDate, endDate));
        validateValidDate(startDate, endDate);
    }

    private void validateValidDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new CustomException(null, ILLEGAL_ARGUMENT_DATE_SET);
        }
    }

    boolean isSameDate() {
        return startDate.equals(endDate);
    }
}