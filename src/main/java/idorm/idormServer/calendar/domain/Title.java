package idorm.idormServer.calendar.domain;

import static idorm.idormServer.common.exception.ExceptionCode.TITLE_LENGTH_INVALID;

import idorm.idormServer.common.util.Validator;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "value")
public class Title {

    private static final int MIN_LENGTH = 1;
    public static final int MAX_OFFICIAL_CALENDAR_LENGTH = 20;
    private static final int MAX_TEAM_CALENDAR_LENGTH = 15;

    private String value;

    public Title(final String value) {
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

    public static Title forMapper(final String title) {
        return new Title(title);
    }

    public void updateOfficialCalendar(String value){
        validateOfficialCalendar(value);
        this.value = value;
    }

    public void updateTeamCalendar(String value){
        validateTeamCalendar(value);
        this.value = value;
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