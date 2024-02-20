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
import idorm.idormServer.calendar.application.port.out.LoadSleepoverCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.application.port.out.SaveSleepoverCalendarPort;
import idorm.idormServer.calendar.domain.SleepoverCalendar;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SleepoverCalendarService implements SleepoverCalendarUseCase {

	private final LoadMemberPort loadMemberPort;

	private final LoadTeamPort loadTeamPort;

	private final SaveSleepoverCalendarPort saveSleepoverCalendarPort;
	private final LoadSleepoverCalendarPort loadSleepoverCalendarPort;

	@Override
	public SleepoverCalendarResponse save(final AuthResponse authResponse, final SaveSleepoverCalendarRequest request) {
		final Team team = loadTeamPort.findByMemberIdWithTeamMember(authResponse.getId());

		// TODO: 서비스에서 validate 메서드 호출 지양
		validateUniqueDate(authResponse.getId(), request.periodOf());
		SleepoverCalendar sleepoverCalendar = request.from(authResponse.getId(), team);
		saveSleepoverCalendarPort.save(sleepoverCalendar);

		return SleepoverCalendarResponse.of(sleepoverCalendar,
			participant(sleepoverCalendar.getParticipant().getMemberId()));
	}

	@Override
	public SleepoverCalendarResponse update(final AuthResponse authResponse,
		final UpdateSleepoverCalendarRequest request) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final SleepoverCalendar updateSleepoverCalendar = loadSleepoverCalendarPort.findByIdAndMemberId(
			request.teamCalendarId(), member.getId());

		// TODO: 서비스에서 validate 메서드 호출 지양
		validateUniqueDate(member.getId(), request.period());
		updateSleepoverCalendar.update(request.period());

		return SleepoverCalendarResponse.of(updateSleepoverCalendar,
			participant(updateSleepoverCalendar.getParticipant().getMemberId()));
	}

	@Override
	public void delete(final AuthResponse authResponse, final Long sleepoverCalendarId) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Team team = loadTeamPort.findByMemberIdWithTeamMember(member.getId());

		// TODO : sleepoverCalendarId, memberId 로 조회. 없으면 NOT_FOUND
		final SleepoverCalendar sleepoverCalendar = loadSleepoverCalendarPort.findByIdAndMemberId(sleepoverCalendarId,
			authResponse.getId());

		// TODO : DeletePort delete 날리기
		// sleepoverCalendar.delete();
	}

	@Override
	public SleepoverCalendarResponse findById(final AuthResponse authResponse, final Long sleepoverCalendarId) {
		final SleepoverCalendar sleepoverCalendar = loadSleepoverCalendarPort.findByIdAndMemberId(sleepoverCalendarId,
			authResponse.getId());

		return SleepoverCalendarResponse.of(sleepoverCalendar,
			participant(sleepoverCalendar.getParticipant().getMemberId()));
	}

	@Override
	public List<SleepoverCalendarResponse> findSleepoverCalendarsByMonth(final AuthResponse authResponse,
		final FindSleepoverCalendarsRequest request) {
		final Team team = loadTeamPort.findByMemberIdWithTeamMember(authResponse.getId());
		final List<SleepoverCalendar> sleepoverCalendars = loadSleepoverCalendarPort.findByYearMonth(team,
			request.yearMonth());

		List<SleepoverCalendarResponse> responses = sleepoverCalendars.stream()
			.map(sleepoverCalendar -> SleepoverCalendarResponse.of(sleepoverCalendar,
				participant(sleepoverCalendar.getParticipant().getMemberId())))
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
	// private void validateSleepoverCalendarAccessMember(Member member, Team team,
	//     SleepoverCalendar sleepoverCalendar) {
	//   team.validateTeamAccessMember(member);
	//   sleepoverCalendar.validateSleepoverCalendarAceessMember(member);
	// }
}
