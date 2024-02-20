package idorm.idormServer.calendar.adapter.out.persistence;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCalendarJpaEntity {

    @Id
    @Column(name = "team_schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PeriodEmbeddedEntity period;

    @Embedded
    private DurationEmbeddedEntity duration;

    @Embedded
    private TitleEmbeddedEntity title;

    @Embedded
    private CalendarContentEmbeddedEntity content;

    @Embedded
    private ParticipantsEmbeddedEntity participants;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private TeamJpaEntity team;
}
