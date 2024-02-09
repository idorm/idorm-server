package idorm.idormServer.calendar.domain;

import idorm.idormServer.calendar.dto.RoomMateCalendarUpdateRequest;
import idorm.idormServer.calendar.dto.SleepoverCalendarUpdateRequest;
import idorm.idormServer.common.domain.BaseTimeEntity;
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
public class TeamCalendar extends BaseTimeEntity {

    @Id
    @Column(name = "team_schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Period period;

    @Embedded
    private Duration duration;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // 일정 참여자들 - 리팩 대상
    @ElementCollection
    @CollectionTable(name = "room_mate_team_calendar_target",
            joinColumns = @JoinColumn(name = "room_mate_team_calendar_id"))
    @Column(name = "target_member_id")
    private List<Long> targets = new ArrayList<>();

    // TODO: 핵심 비지니스 로직 리팩 대상
    @Builder
    public TeamCalendar(LocalDate startDate,
                        LocalDate endDate,
                        LocalTime startTime,
                        LocalTime endTime,
                        String title,
                        String content,
                        Boolean isSleepover,
                        Team team,
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
