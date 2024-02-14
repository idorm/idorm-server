package idorm.idormServer.calendar.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamCalendar {

    private Long id;
    private Period period;
    private Duration duration;
    private Title title;
    private Content content;
    private Participants participants;
    private Team team;

    public TeamCalendar(final Period period,
                        final Duration duration,
                        final Title title,
                        final Content content,
                        final Participants participants,
                        final Team team) {
        this.period = period;
        this.duration = duration;
        this.title = title;
        this.content = content;
        this.participants = participants;
        this.team = team;
    }

    public static TeamCalendar forMapper(final Long id,
                                         final Period period,
                                         final Duration duration,
                                         final Title title,
                                         final Content content,
                                         final Participants participants,
                                         final Team team) {
        return new TeamCalendar(id, period, duration, title, content, participants, team);
    }

    // TODO: 핵심 비지니스 로직 리팩 대상
    public void participate(Long memberId) {
        participants.participate(memberId);
    }

    public void deleteParticipant(Long memberId) {
        this.participants.delete(memberId);
    }

    public void update(String title, String content, LocalDate startDate, LocalDate endDate,
                       LocalTime startTime, LocalTime endTime) {
        this.title.updateTeamCalendar(title);
        this.content.update(content);
        this.period.update(startDate, endDate);
        this.duration.update(this.period, startTime, endTime);
    }

    public void delete() {
        this.delete();
    }
}