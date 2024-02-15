package idorm.idormServer.calendar.application;

import idorm.idormServer.calendar.application.port.in.OfficialCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarUpdateRequest;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarsFindRequest;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfficialCalendarService implements OfficialCalendarUseCase {

	@Override
	public void update(Member member, OfficialCalendarUpdateRequest request) {

	}

	@Override
	public void delete(Member member, Long officialCalendarId) {

	}

	@Override
	public void findAllByAdmin(Member member) {

	}

	@Override
	public void findOneByAdmin(Member member, Long officialCalendarId) {

	}

	@Override
	public void findAllByMember(Member member, OfficialCalendarsFindRequest request) {

	}
}