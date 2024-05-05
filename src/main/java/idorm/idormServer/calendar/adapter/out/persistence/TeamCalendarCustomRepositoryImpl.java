package idorm.idormServer.calendar.adapter.out.persistence;

import static idorm.idormServer.calendar.entity.QParticipant.*;
import static idorm.idormServer.calendar.entity.QTeam.*;
import static idorm.idormServer.calendar.entity.QTeamCalendar.*;
import static idorm.idormServer.notification.entity.QFcmToken.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.calendar.entity.TeamCalendar;
import idorm.idormServer.notification.adapter.out.api.NotificationRequest;
import idorm.idormServer.notification.entity.FcmChannel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamCalendarCustomRepositoryImpl implements TeamCalendarCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public TeamCalendar findByIdAndMemberId(Long teamCalendarId, Long memberId) {
		return queryFactory
			.select(teamCalendar)
			.from(teamCalendar)
			.join(teamCalendar.participants.participants, participant)
			.where(teamCalendar.id.eq(teamCalendarId)
				.and(participant.memberId.eq(memberId)))
			.fetchOne();
	}

	@Override
	public List<TeamCalendar> findByMemberId(Long memberId) {
		return queryFactory
			.select(teamCalendar)
			.from(teamCalendar)
			.join(teamCalendar.team, team)
			.where(teamCalendar.participants.participants.any().memberId.eq(memberId))
			.fetch();
	}

	@Override
	public List<TeamCalendar> findByYearMonth(Team teamDomain, YearMonth yearMonth) {
		return queryFactory
			.select(teamCalendar)
			.from(teamCalendar)
			.join(teamCalendar.team, team)
			.where(team.eq(teamDomain)
				.and(teamCalendar.period.startDate.month().eq(yearMonth.getMonthValue()))
				.or(teamCalendar.period.endDate.month().eq(yearMonth.getMonthValue())))
			.fetch();
	}

	@Override
	public List<NotificationRequest> findByStartDateIsToday() {
		List<Tuple> results = queryFactory
			.select(teamCalendar.id, teamCalendar.title, fcmToken.value)
			.from(teamCalendar)
			.join(teamCalendar.participants.participants, participant)
			.join(fcmToken)
			.on(fcmToken.memberId.eq(participant.memberId))
			.where(teamCalendar.period.startDate.eq(LocalDate.now())
				.and(teamCalendar.participants.participants.isNotEmpty()))
			.fetch();

		List<NotificationRequest> requests = results.stream()
			.map(tuple -> {
				NotificationRequest request = NotificationRequest.builder()
					.token(tuple.get(fcmToken.value))
					.message(NotificationRequest.NotificationMessage.builder()
						.notifyType(FcmChannel.TEAMCALENDAR)
						.contentId(tuple.get(teamCalendar.id))
						.title(FcmChannel.TEAMCALENDAR.getTitle())
						.content(tuple.get(teamCalendar.title))
						.build())
					.build();
				return request;
			})
			.toList();

		return requests;
	}
}