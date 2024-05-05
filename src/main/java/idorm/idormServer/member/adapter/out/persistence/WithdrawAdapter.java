package idorm.idormServer.member.adapter.out.persistence;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import idorm.idormServer.auth.application.port.out.DeleteRefreshTokenPort;
import idorm.idormServer.calendar.application.port.out.DeleteSleepoverCalendarPort;
import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.email.application.port.out.DeleteEmailPort;
import idorm.idormServer.email.entity.Email;
import idorm.idormServer.matchingInfo.application.port.out.DeleteMatchingInfoPort;
import idorm.idormServer.matchingMate.application.port.out.DeleteMatchingMatePort;
import idorm.idormServer.member.application.port.out.WithdrawPort;
import idorm.idormServer.member.entity.Member;
import idorm.idormServer.notification.application.port.out.DeleteFcmTokenPort;
import idorm.idormServer.photo.application.port.out.DeleteFilePort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WithdrawAdapter implements WithdrawPort {

	private final DeleteEmailPort deleteEmailPort;
	private final DeleteFilePort deleteFilePort;
	private final DeleteRefreshTokenPort deleteRefreshTokenPort;
	private final DeleteFcmTokenPort deleteFcmTokenPort;
	private final DeleteMatchingMatePort deleteMatchingMatePort;
	private final DeleteMatchingInfoPort deleteMatchingInfoPort;
	private final DeleteSleepoverCalendarPort deleteSleepoverCalendarPort;

	@Override
	public void withdraw(Member member, Email email) {
		deleteRefreshTokenPort.deleteAll(member.getId());
		deleteEmailPort.delete(email);
		deleteFcmTokenPort.deleteAll(member.getId());
		deleteMatchingMatePort.deleteByMemberWithdrawn(member);
		deleteMatchingInfoPort.delete(member.getMatchingInfo().getId());

		if (StringUtils.hasLength(member.getProfilePhotoUrl())) {
			deleteFilePort.deleteMemberPhotoFile(member.getProfilePhotoUrl());
		}

		/**
		 * TODO: Team 내 Member 제거 시나리오
		 *
		 * 1. 팀이 존재하지 않는 경우
		 * 		: 바로 반환
		 *
		 * 2. 팀이 존재하는 경우
		 * 		a) 탈퇴 회원이 마지막 회원인 경우 (Team.team_status == ALONE)
		 *         0. [a, b, c 공통] SleepoverCalendar : 모든 외박캘린더 삭제
		 *         1. TeamCalendar : 모든 TeamCalendar 삭제
		 *         		- Participant : 각 TeamCalendar를 가진 모든 Participant 삭제
		 *         2. Team : TeamStatus == DELETED 변경
		 *
		 *      b) 탈퇴 회원 삭제 시 한명만 팀에 남는 경우
		 *         0. [a, b, c 공통] SleepoverCalendar : 모든 외박캘린더 삭제
		 *         1. [b, c 공통] Participant : 탈퇴 회원을 가진 모든 Participant 삭제
		 *         2. [b, c 공통] TeamCalendar : Participant가 하나도 없는 경우 삭제
		 *         3. Team : TeamStatus == ALONE 변경
		 *
		 *      c) 탈퇴 회원 삭제 후 두 명 이상 팀에 있는 경우
		 *         0. [a, b, c 공통] SleepoverCalendar : 모든 외박캘린더 삭제
		 *         1. [b, c 공통] Participant : 탈퇴 회원을 가진 Participant 삭제
		 *         2. [b, c 공통] TeamCalendar : Participant가 하나도 없는 경우 삭제
		 *         3. Team : TeamStatus == ACTIVE 유지
		 */
		if (Objects.isNull(member.getTeam())) {
			// no-op
		} else {
			deleteSleepoverCalendarPort.deleteByMemberId(member.getId());

			if (member.getTeam().getTeamStatus().isAlone()) {
				explodeTeam(member.getTeam());
			}

			// TODO -CASE 1: 탈퇴 회원 삭제 시 한 명만 남는 경우
			// TODO -CASE 2: 탈퇴 회원 삭제 시 두 명 이상 남는 경우
		}

		member.withdraw();
	}

	private void explodeTeam(Team team) {
		// TODO:
	}
}
