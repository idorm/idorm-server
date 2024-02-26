package idorm.idormServer.calendar.entity;

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

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import idorm.idormServer.calendar.adapter.out.exception.AccessDeniedTeamException;
import idorm.idormServer.calendar.adapter.out.exception.CannotExplodeTeamException;
import idorm.idormServer.calendar.adapter.out.exception.CannotRegisterTeamStatusFullException;
import idorm.idormServer.member.adapter.out.exception.DuplicatedMemberException;
import idorm.idormServer.member.adapter.out.exception.NotFoundMemberException;
import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

	private static final int MAX_TEAM_SIZE = 4;

	@Id
	@Column(name = "team_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private TeamStatus teamStatus;

	@OneToMany(mappedBy = "team")
	private List<Member> members = new ArrayList<>();

	@OneToMany(mappedBy = "team")
	private List<TeamCalendar> teamCalendars = new ArrayList<>();

	@OneToMany(mappedBy = "team")
	private List<SleepoverCalendar> sleepoverCalendars = new ArrayList<>();

	@CreatedDate
	@JsonIgnore
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime createdAt;

	public Team(final List<Member> members) {
		teamStatus = TeamStatus.ACTIVE;
		this.members.addAll(members);
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