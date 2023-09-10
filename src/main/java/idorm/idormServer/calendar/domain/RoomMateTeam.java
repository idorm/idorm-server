package idorm.idormServer.calendar.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomMateTeam extends BaseEntity {

    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean isNeedToConfirmDeleted; // 최후의 1인이 팀 폭발여부 확인했는지 여부

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<TeamCalendar> teamCalendars = new ArrayList<>();

    @Builder
    public RoomMateTeam(Member member) {
        addMember(member);
        member.updateTeam(this, 0);
        this.isNeedToConfirmDeleted = false;
        this.setIsDeleted(false);
    }

    public void addMember(Member member) {
        if (!this.members.contains(member)) {
            this.members.add(member);
            member.updateTeam(this, this.members.size() - 1);
        }
    }

    public boolean removeMember(Member member) {
        if (this.members.contains(member)) {
            this.members.remove(member);
            member.deleteTeam(this);
            return true;
        }
        return false;
    }

    public List<Member> getMembers() {
        return this.members;
    }

    public int getMemberCount() {
        return this.members.size();
    }

    public void addTeamCalendar(TeamCalendar teamCalendar) {
        this.teamCalendars.add(teamCalendar);
    }

    public void removeTeamCalendar(TeamCalendar teamCalendar) {
        this.teamCalendars.remove(teamCalendar);
    }

    public List<TeamCalendar> getTeamCalendars() {
        List<TeamCalendar> teamCalendarList = this.teamCalendars;
        teamCalendarList.removeIf(teamCalendar -> teamCalendar.getIsDeleted().equals(true));
        return teamCalendarList;
    }

    public void updateIsNeedToConfirmDeleted() {
        this.isNeedToConfirmDeleted = true;
    }

    public void delete() {
        this.setIsDeleted(true);

        for (Member member : this.members)
            member.deleteTeam(this);

        List<TeamCalendar> deleteList = this.getTeamCalendars().stream().collect(Collectors.toList());
        for (TeamCalendar teamCalendar : deleteList)
            teamCalendar.delete();

    }
}
