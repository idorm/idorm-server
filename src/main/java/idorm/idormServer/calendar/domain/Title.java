package idorm.idormServer.calendar.domain;

import static idorm.idormServer.common.exception.ExceptionCode.TITLE_LENGTH_INVALID;

import idorm.idormServer.common.util.Validator;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_OFFICIAL_CALENDAR_LENGTH = 20;
    private static final int MAX_TEAM_CALENDAR_LENGTH = 15;

    @NotBlank
    @Column(name = "title", nullable = false, length = MAX_OFFICIAL_CALENDAR_LENGTH)
    private String value;

    private Title(final String value) {
        this.value = value;
    }

    public static Title officialCalendar(final String value) {
        validateOfficialCalendar(value);
        return new Title(value);
    }

    public static Title teamCalendar(final String value) {
        validateTeamCalendar(value);
        return new Title(value);
    }

    boolean notEquals(final String value) {
        return this.value.equals(value);
    }

    private static void validateOfficialCalendar(String value) {
        Validator.validateNotBlank(value);
        Validator.validateLength(value, MIN_LENGTH, MAX_OFFICIAL_CALENDAR_LENGTH, TITLE_LENGTH_INVALID);
    }

    private static void validateTeamCalendar(String value) {
        Validator.validateNotBlank(value);
        Validator.validateLength(value, MIN_LENGTH, MAX_TEAM_CALENDAR_LENGTH, TITLE_LENGTH_INVALID);
    }
}