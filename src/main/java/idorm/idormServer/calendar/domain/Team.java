package idorm.idormServer.calendar.domain;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Team {

    private static final int MAX_TEAM_SIZE = 4;

    private Long id;
    private TeamStatus teamStatus;
    private List<Member> members = new ArrayList<>();
    private List<TeamCalendar> teamCalendars = new ArrayList<>();
    private List<SleepoverCalendar> sleepoverCalendars = new ArrayList<>();
    private LocalDateTime createdAt;

    public Team(final List<Member> members) {
        teamStatus = TeamStatus.ACTIVE;
        this.members = members;
        this.createdAt = LocalDateTime.now();
    }

    public static Team forMapper(final Long id,
                                 final TeamStatus teamStatus,
                                 final List<Member> members,
                                 final List<TeamCalendar> teamCalendars,
                                 final List<SleepoverCalendar> sleepoverCalendars,
                                 final LocalDateTime createdAt) {
        return new Team(id, teamStatus, members, teamCalendars, sleepoverCalendars, createdAt);
    }

    public void canRemoveMember(Member member){
        if(!this.members.contains(member)) {
            throw new CustomException(null, ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    public void participate(Member member) {
        if(this.members.contains(member)){
            throw new CustomException(null, ExceptionCode.DUPLICATE_MEMBER);
        }
        validateValidSize();
    }

    public int getTeamSize(){
        return this.getMembers().size();
    }

    public boolean isTeamMember(Member member) {
        if(!this.members.contains(member)){
            throw new CustomException(null, ExceptionCode.TEAMMEMBER_NOT_FOUND);
        }
        return this.members.contains(member);
    }

    public void changeTeamStatusToAlone() {
        this.teamStatus = TeamStatus.ALONE;
    }

    public Team isTeamExists(){
        if(this.id == null){
            throw new CustomException(null, ExceptionCode.TEAM_NOT_FOUND);
        }
        return this;
    }


    public void delete() {
        this.teamCalendars.stream().forEach(teamCalendar -> deleteTeamSchedule(teamCalendar));
        this.sleepoverCalendars.stream().forEach(sleepoverCalendar -> deleteSleepOverSchedule(sleepoverCalendar));
        this.members.stream().forEach(member -> this.members.remove(member));
        this.delete();
    }

    public void deleteTeamSchedule(TeamCalendar teamCalendar){
        this.teamCalendars.remove(teamCalendar);
    }

    public void deleteSleepOverSchedule(SleepoverCalendar sleepoverCalendare){
        this.sleepoverCalendars.remove(sleepoverCalendare);
    }


    private void validateValidSize() {
        if(this.members.size() >= MAX_TEAM_SIZE) {
            throw new CustomException(null, ExceptionCode.CANNOT_REGISTER_TEAM_STATUS_FULL);
        }
    }

    public List<TeamCalendar> getTeamSchedules(){
        return this.teamCalendars;
    }

    public List<SleepoverCalendar> getSleepoverSchedules(){
        return this.sleepoverCalendars;
    }
}