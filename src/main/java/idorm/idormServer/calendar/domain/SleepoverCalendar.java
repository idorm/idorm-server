package idorm.idormServer.calendar.domain;

import idorm.idormServer.common.util.Validator;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SleepoverCalendar {

    private Long id;
    private Period period;
    private Participant participant;
    private Team team;

    public SleepoverCalendar(final Period period, final Participant participant, final Team team) {
        validateConstructor(period, participant, team);
        this.period = period;
        this.participant = participant;
        this.team = team;
    }

    public void assignId(Long generatedId) {
        this.id = generatedId;
    }

    public static SleepoverCalendar forMapper(final Long id,
                                              final Period period,
                                              final Participant participant,
                                              final Team team) {
        return new SleepoverCalendar(id, period, participant, team);
    }

    private void validateConstructor(Period period, Participant participant, Team team) {
        Validator.validateNotNull(List.of(period, participant, team));
    }

    public void validateUniqueDate(Period newPeriod) {
        this.period.validateUniqueDate(newPeriod);
    }

    public void delete() {
        this.delete();
    }
}