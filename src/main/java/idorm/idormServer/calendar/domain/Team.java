package idorm.idormServer.calendar.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import idorm.idormServer.calendar.adapter.out.exception.AccessDeniedTeamException;
import idorm.idormServer.calendar.adapter.out.exception.CannotExplodeTeamException;
import idorm.idormServer.calendar.adapter.out.exception.CannotRegisterTeamStatusFullException;
import idorm.idormServer.member.adapter.out.exception.DuplicatedMemberException;
import idorm.idormServer.member.adapter.out.exception.NotFoundMemberException;
import idorm.idormServer.member.domain.Member;
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

	/*  public Team(
		  final Long id,
		  final TeamStatus teamStatus,
		  final List<Member> members,
		  final List<TeamCalendar> teamCalendars,
		  final List<SleepoverCalendar> sleepoverCalendars,
		  final LocalDateTime createdAt) {
		this.id = id;
		this.teamStatus = teamStatus;
		this.members = members;
		this.teamCalendars = teamCalendars;
		this.sleepoverCalendars = sleepoverCalendars;
		this.createdAt =createdAt;
	  }*/
	public Team(final List<Member> members) {
		teamStatus = TeamStatus.ACTIVE;
		this.members = members;
	}

	public static Team forMapper(final Long id,
		final TeamStatus teamStatus,
		final List<Member> members,
		final List<TeamCalendar> teamCalendars,
		final List<SleepoverCalendar> sleepoverCalendars,
		final LocalDateTime createdAt) {
		return new Team(id, teamStatus, members, teamCalendars, sleepoverCalendars, createdAt);
	}

	public void addMember(final Member member) {
		validateTeamSize();
		validateParticipationInTeam(member);
		this.members.add(member);
	}

	public void deleteMember(Member member) {
		if (!this.members.contains(member)) {
			throw new NotFoundMemberException(); // TODO: 예외 변경 : 접근 권한?
		}
		this.members.remove(member);
		if (this.members.size() == 1) {
			this.teamStatus = TeamStatus.ALONE;
		}
	}

	public void delete() {
		if (TeamStatus.isNotAlone(this.teamStatus)) {
			throw new CannotExplodeTeamException();
		}
		this.delete();
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