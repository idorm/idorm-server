package idorm.idormServer.calendar.entity;

import idorm.idormServer.calendar.adapter.out.exception.AccessDeniedTeamException;
import idorm.idormServer.calendar.adapter.out.exception.AlreadyDeletedMemberException;
import idorm.idormServer.calendar.adapter.out.exception.CannotExplodeTeamException;
import idorm.idormServer.calendar.adapter.out.exception.CannotRegisterTeamStatusFullException;
import idorm.idormServer.common.entity.BaseTimeEntity;
import idorm.idormServer.calendar.adapter.out.exception.DuplicatedMemberException;
import idorm.idormServer.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseTimeEntity {

  private static final int MAX_TEAM_SIZE = 4;

  @Id
  @Column(name = "team_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private TeamStatus teamStatus;

  @OneToMany(mappedBy = "team")
  private List<Member> members = new ArrayList<>();

  @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<TeamCalendar> teamCalendars = new ArrayList<>();

  @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<SleepoverCalendar> sleepoverCalendars = new ArrayList<>();

  public Team(final Member member) {
    teamStatus = TeamStatus.ACTIVE;
    this.members.add(member);
    member.addTeam(this);
  }

  public void addMember(final Member member) {
    validateTeamSize();
    validateParticipationInTeam(member);
    this.members.add(member);
    member.addTeam(this);
  }

  public void deleteMember(Member member) {
    if (!this.members.contains(member)) {
      throw new AlreadyDeletedMemberException();
    }
    member.removeTeam();
    if (this.members.size() == 1) {
      this.teamStatus = TeamStatus.ALONE;
    }
  }

  public void delete(Member member) {
    if (TeamStatus.isNotAlone(this.teamStatus)) {
      throw new CannotExplodeTeamException();
    }
    member.removeTeam();
  }


  public void validateTeamAccessMember(Member member) {
    if (!this.members.contains(member)) {
      throw new AccessDeniedTeamException();
    }
  }

  private void validateParticipationInTeam(Member member) {
    if (this.members.contains(member)) {
      throw new DuplicatedMemberException();
    }
  }

  private void validateTeamSize() {
    if (this.members.size() >= MAX_TEAM_SIZE) {
      throw new CannotRegisterTeamStatusFullException();
    }
  }
}