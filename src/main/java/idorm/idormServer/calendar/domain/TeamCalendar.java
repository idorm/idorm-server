package idorm.idormServer.calendar.domain;

import idorm.idormServer.calendar.dto.TeamCalendar.TeamCalendarUpdateRequestDto;
import idorm.idormServer.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCalendar extends BaseEntity {

    @Id
    @Column(name = "team_calendar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ElementCollection
    @CollectionTable(name = "targets",
            joinColumns = @JoinColumn(name = "team_calendar_id"))
    @Column(name = "team_calendar_target")
    private List<Long> targets = new ArrayList<>(); // 일정 대상자들

    @Builder
    public TeamCalendar(LocalDate startDate,
                        LocalDate endDate,
                        LocalTime startTime,
                        LocalTime endTime,
                        String title,
                        String content,
                        Team team,
                        List<Long> targets) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.content = content;

        for (Long target : targets)
            this.targets.add(target);

        this.setIsDeleted(false);

        this.team = team;

        if (!team.getTeamCalendars().contains(this))
            team.addTeamCalendar(this);
    }

    public void updateContents(TeamCalendarUpdateRequestDto request, List<Long> targets) {
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.title = request.getTitle();
        this.content = request.getContent();

        this.updateTargets(targets);
    }

    private void updateTargets(List<Long> newTargets) {
        this.targets.clear();
        for (Long target : newTargets)
            this.targets.add(target);
    }

    public void deleteTarget(Long target) {
        if (this.targets.contains(target))
            this.targets.remove(target);
    }

    public void delete() {
        this.setIsDeleted(true);
        if (team.getTeamCalendars().contains(this))
            this.team.removeTeamCalendar(this);
    }
}
