package idorm.idormServer.calendar.adapter.out.persistence;

import static idorm.idormServer.calendar.entity.QParticipant.participant;
import static idorm.idormServer.calendar.entity.QTeam.team;
import static idorm.idormServer.calendar.entity.QTeamCalendar.teamCalendar;

import com.querydsl.jpa.impl.JPAQueryFactory;
import idorm.idormServer.calendar.adapter.out.exception.NotFoundTeamCalendarException;
import idorm.idormServer.calendar.application.port.out.LoadTeamCalendarPort;
import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.calendar.entity.TeamCalendar;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadTeamCalendarAdapter implements LoadTeamCalendarPort {

  private final JPAQueryFactory queryFactory;
  private final TeamCalendarRepository teamCalendarRepository;


  @Override
  public TeamCalendar findByIdAndTeamId(Long teamCalendarId, Long teamId) {
    TeamCalendar response = teamCalendarRepository.findByIdAndTeamId(teamCalendarId, teamId)
        .orElseThrow(NotFoundTeamCalendarException::new);
    return response;
  }

  @Override
  public TeamCalendar findByIdAndMemberId(Long teamCalendarId, Long memberId) {
    TeamCalendar response = queryFactory
        .select(teamCalendar)
        .from(teamCalendar)
        .join(teamCalendar.participants.participants, participant)
        .where(teamCalendar.id.eq(teamCalendarId)
            .and(participant.memberId.eq(memberId)))
        .fetchOne();

    if (response == null) {
      throw new NotFoundTeamCalendarException();
    }
    return response;
  }

  @Override
  public List<TeamCalendar> findByMemberId(Long memberId) {
    List<TeamCalendar> responses = queryFactory
        .select(teamCalendar)
        .from(teamCalendar)
        .join(teamCalendar.team, team)
        .where(teamCalendar.participants.participants.any().memberId.eq(memberId))
        .fetch();
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

  @Override
  public List<TeamCalendar> findByTeamId(Long teamId) {
    List<TeamCalendar> responses = teamCalendarRepository.findByTeamId(teamId);
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

  @Override
  public List<TeamCalendar> findByYearMonth(Team teamDomain, YearMonth yearMonth) {
    List<TeamCalendar> responses = queryFactory
        .select(teamCalendar)
        .from(teamCalendar)
        .join(teamCalendar.team, team)
        .where(team.eq(teamDomain)
            .and(teamCalendar.period.startDate.month().eq(yearMonth.getMonthValue()))
            .or(teamCalendar.period.endDate.month().eq(yearMonth.getMonthValue())))
        .fetch();
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

  @Override
  public List<TeamCalendar> findByStartDateIsToday() {
    //TODO: 구현
    return null;
  }
}
