package idorm.idormServer.calendar.domain;

import idorm.idormServer.calendar.dto.RoomMateCalendarUpdateRequest;
import idorm.idormServer.calendar.dto.SleepoverCalendarUpdateRequest;
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
public class RoomMateTeamCalendar extends BaseEntity {

    @Id
    @Column(name = "room_mate_team_calendar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String title;
    private String content;
    private Boolean isSleepover;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_mate_team_id")
    private RoomMateTeam roomMateTeam;

    @ElementCollection
    @CollectionTable(name = "room_mate_team_calendar_target",
            joinColumns = @JoinColumn(name = "room_mate_team_calendar_id"))
    @Column(name = "member_id")
    private List<Long> targets = new ArrayList<>(); // 일정 대상자들

    @Builder
    public RoomMateTeamCalendar(LocalDate startDate,
                                LocalDate endDate,
                                LocalTime startTime,
                                LocalTime endTime,
                                String title,
                                String content,
                                Boolean isSleepover,
                                RoomMateTeam team,
                                List<Long> targets) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.content = content;
        this.isSleepover = isSleepover;

        for (Long target : targets)
            this.targets.add(target);

        this.setIsDeleted(false);

        this.roomMateTeam = team;

        if (!team.getTeamCalendars().contains(this))
            team.addTeamCalendar(this);
    }

    public void updateContents(RoomMateCalendarUpdateRequest request, List<Long> targets) {
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.title = request.getTitle();
        this.content = request.getContent();

        this.updateTargets(targets);
    }

    public void updateDates(SleepoverCalendarUpdateRequest request) {
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
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
        if (roomMateTeam.getTeamCalendars().contains(this))
            this.roomMateTeam.removeTeamCalendar(this);
    }
}
