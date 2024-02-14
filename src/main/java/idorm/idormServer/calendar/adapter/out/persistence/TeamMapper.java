package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.SleepoverCalendar;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import idorm.idormServer.member.adapter.out.persistence.MemberJpaEntity;
import idorm.idormServer.member.adapter.out.persistence.MemberMapper;
import idorm.idormServer.member.domain.Member;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMapper {

    private final MemberMapper memberMapper;
    private final TeamCalendarMapper teamCalendarMapper;
    private final SleepoverCalendarMapper sleepoverCalendarMapper;

    public TeamJpaEntity toEntity(Team team) {

        return new TeamJpaEntity(team.getId(),
                team.getTeamStatus(),
                convertMemberEntity(team.getMembers()),
                convertTeamCalendarEntity(team.getTeamCalendars()),
                convertSleepoverCalendarEntity(team.getSleepoverCalendars()),
                team.getCreatedAt());
    }


    public Team toDomain(TeamJpaEntity teamJpaEntity) {
        return Team.forMapper(teamJpaEntity.getId(),
                teamJpaEntity.getTeamStatus(),
                convertMember(teamJpaEntity.getMembers()),
                convertTeamCalendar(teamJpaEntity.getTeamCalendars()),
                convertSleepoverCalendar(teamJpaEntity.getSleepoverCalendars()),
                teamJpaEntity.getCreatedAt());
    }

    private List<MemberJpaEntity> convertMemberEntity(List<Member> members) {
        List<MemberJpaEntity> result = members.stream()
                .map(memberMapper::toEntity)
                .toList();

        return result;
    }

    private List<Member> convertMember(List<MemberJpaEntity> members) {
        List<Member> result = members.stream()
                .map(memberMapper::toDomain)
                .toList();
        return result;
    }

    private List<TeamCalendarJpaEntity> convertTeamCalendarEntity(List<TeamCalendar> teamCalendars) {
        List<TeamCalendarJpaEntity> result = teamCalendars.stream()
                .map(teamCalendarMapper::toEntity)
                .toList();
        return result;
    }

    private List<TeamCalendar> convertTeamCalendar(List<TeamCalendarJpaEntity> teamCalendars) {
        List<TeamCalendar> result = teamCalendars.stream()
                .map(teamCalendarMapper::toDomain)
                .toList();
        return result;
    }

    private List<SleepoverCalendarJpaEntity> convertSleepoverCalendarEntity(List<SleepoverCalendar> calendars) {
        List<SleepoverCalendarJpaEntity> result = calendars.stream()
                .map(sleepoverCalendarMapper::toEntity)
                .toList();
        return result;
    }

    private List<SleepoverCalendar> convertSleepoverCalendar(List<SleepoverCalendarJpaEntity> calendars) {
        List<SleepoverCalendar> result = calendars.stream()
                .map(sleepoverCalendarMapper::toDomain)
                .toList();
        return result;
    }
}