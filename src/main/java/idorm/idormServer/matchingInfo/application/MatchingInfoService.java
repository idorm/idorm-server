package idorm.idormServer.matchingInfo.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.matchingInfo.application.port.in.MatchingInfoUseCase;
import idorm.idormServer.matchingInfo.application.port.in.dto.MatchingInfoRequest;
import idorm.idormServer.matchingInfo.application.port.in.dto.MatchingInfoResponse;
import idorm.idormServer.matchingInfo.application.port.in.dto.MatchingInfoVisibilityRequest;
import idorm.idormServer.matchingInfo.application.port.out.DeleteMatchingInfoPort;
import idorm.idormServer.matchingInfo.application.port.out.LoadMatchingInfoPort;
import idorm.idormServer.matchingInfo.application.port.out.SaveMatchingInfoPort;
import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingInfoService implements MatchingInfoUseCase {

	private final LoadMemberPort loadMemberPort;
	private final LoadMatchingInfoPort loadMatchingInfoPort;
	private final SaveMatchingInfoPort saveMatchingInfoPort;
	private final DeleteMatchingInfoPort deleteMatchingInfoPort;

	@Override
	@Transactional
	public MatchingInfoResponse save(final AuthResponse auth, final MatchingInfoRequest request) {
		final Member member = loadMemberPort.loadMember(auth.getId());
		loadMatchingInfoPort.validateNotExistence(member.getId());

		final MatchingInfo matchingInfo = request.from(member);
		saveMatchingInfoPort.save(matchingInfo);

		return MatchingInfoResponse.from(matchingInfo);
	}

	@Override
	@Transactional
	public MatchingInfoResponse editAll(final AuthResponse auth, final MatchingInfoRequest request) {
		final Member member = loadMemberPort.loadMember(auth.getId());

		MatchingInfo matchingInfo = loadMatchingInfoPort.load(member.getId());
		request.editAll(matchingInfo);

		return MatchingInfoResponse.from(matchingInfo);
	}

	@Override
	@Transactional
	public void editVisibility(final AuthResponse auth, final MatchingInfoVisibilityRequest request) {
		final Member member = loadMemberPort.loadMember(auth.getId());

		MatchingInfo matchingInfo = loadMatchingInfoPort.load(member.getId());
		matchingInfo.editVisibility(request.isMatchingInfoPublic());
	}

	@Override
	public MatchingInfoResponse getInfo(final AuthResponse auth) {
		final Member member = loadMemberPort.loadMember(auth.getId());
		return MatchingInfoResponse.from(loadMatchingInfoPort.load(member.getId()));
	}

	@Override
	@Transactional
	public void delete(final AuthResponse auth) {
		loadMemberPort.loadMember(auth.getId());
		MatchingInfo matchingInfo = loadMatchingInfoPort.load(auth.getId());
		deleteMatchingInfoPort.delete(matchingInfo);
	}
}