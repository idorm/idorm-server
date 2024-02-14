package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.TeamStatus;
import idorm.idormServer.member.adapter.out.persistence.MemberJpaEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamJpaEntity {

    private static final int MAX_TEAM_SIZE = 4;

    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TeamStatus teamStatus;

    @OneToMany(mappedBy = "team")
    private List<MemberJpaEntity> members = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<TeamCalendarJpaEntity> teamCalendars = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<SleepoverCalendarJpaEntity> sleepoverCalendars = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;
}