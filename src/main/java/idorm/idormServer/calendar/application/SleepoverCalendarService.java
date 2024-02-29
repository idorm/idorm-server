package idorm.idormServer.calendar.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.SleepoverCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.FindSleepoverCalendarsRequest;
import idorm.idormServer.calendar.application.port.in.dto.SaveSleepoverCalendarRequest;
import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarParticipantResponse;
import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.UpdateSleepoverCalendarRequest;
import idorm.idormServer.calendar.application.port.out.DeleteSleepoverCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadSleepoverCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.application.port.out.SaveSleepoverCalendarPort;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SleepoverCalendarService implements SleepoverCalendarUseCase {

	private final LoadMemberPort loadMemberPort;

	private final LoadTeamPort loadTeamPort;

	private final SaveSleepoverCalendarPort saveSleepoverCalendarPort;
	private final LoadSleepoverCalendarPort loadSleepoverCalendarPort;
	private final DeleteSleepoverCalendarPort deleteSleepoverCalendarPort;

	@Override
	@Transactional
	public SleepoverCalendarResponse save(final AuthResponse authResponse, final SaveSleepoverCalendarRequest request) {
		final Team team = loadTeamPort.findByMemberId(authResponse.getId());

		loadSleepoverCalendarPort.hasOverlappingDates(authResponse.getId(), request.period(), null);
		SleepoverCalendar sleepoverCalendar = request.from(authResponse.getId(), team);
		saveSleepoverCalendarPort.save(sleepoverCalendar);

		return SleepoverCalendarResponse.of(sleepoverCalendar,
			participant(sleepoverCalendar.getMemberId()));
	}

	@Override
	@Transactional
	public SleepoverCalendarResponse update(final AuthResponse authResponse,
		final UpdateSleepoverCalendarRequest request) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final SleepoverCalendar sleepoverCalendar =
				loadSleepoverCalendarPort.findByIdAndMemberId(request.teamCalendarId(), member.getId());

		loadSleepoverCalendarPort.hasOverlappingDates(member.getId(), request.period(), sleepoverCalendar.getId());
		sleepoverCalendar.update(request.period());

		return SleepoverCalendarResponse.of(sleepoverCalendar,
			participant(sleepoverCalendar.getMemberId()));
	}

	@Override
	@Transactional
	public void delete(final AuthResponse authResponse, final Long sleepoverCalendarId) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final SleepoverCalendar sleepoverCalendar = loadSleepoverCalendarPort.findByIdAndMemberId(sleepoverCalendarId,
				member.getId());

		deleteSleepoverCalendarPort.delete(sleepoverCalendar);
	}

	@Override
	public SleepoverCalendarResponse findById(final AuthResponse authResponse, final Long sleepoverCalendarId) {
		final SleepoverCalendar sleepoverCalendar = loadSleepoverCalendarPort.findByIdAndMemberId(sleepoverCalendarId,
			authResponse.getId());

		return SleepoverCalendarResponse.of(sleepoverCalendar,
			participant(sleepoverCalendar.getMemberId()));
	}

	@Override
	public List<SleepoverCalendarResponse> findSleepoverCalendarsByMonth(final AuthResponse authResponse,
		final FindSleepoverCalendarsRequest request) {
		final Team team = loadTeamPort.findByMemberId(authResponse.getId());
		final List<SleepoverCalendar> sleepoverCalendars = loadSleepoverCalendarPort.findByYearMonth(team,
			request.yearMonth());

		List<SleepoverCalendarResponse> responses = sleepoverCalendars.stream()
			.map(sleepoverCalendar -> SleepoverCalendarResponse.of(sleepoverCalendar,
				participant(sleepoverCalendar.getMemberId())))
			.toList();
		return responses;
	}

	private SleepoverCalendarParticipantResponse participant(Long memberId) {
		return SleepoverCalendarParticipantResponse.of(loadMemberPort.loadMember(memberId));
	}

	// private void validateUniqueDate(Long memberId, Period period) {
	//   Long count = loadSleepoverCalendarPort.countOverlappingDates(memberId, period);
	//   if (count > 0) {
	//     throw new DuplicatedSleepoverDateException();
	//   }
	// }
	//
	// private void validateSleepoverCalendarAccessMember(Member member, TeamDomain team,
	//     SleepoverCalendar sleepoverCalendar) {
	//   team.validateTeamAccessMember(member);
	//   sleepoverCalendar.validateSleepoverCalendarAceessMember(member);
	// }
}
