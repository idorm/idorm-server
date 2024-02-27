package idorm.idormServer.matchingMate.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.matchingInfo.application.port.out.LoadMatchingInfoPort;
import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import idorm.idormServer.matchingMate.application.port.in.MatchingMateUseCase;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.application.port.in.dto.PreferenceMateRequest;
import idorm.idormServer.matchingMate.application.port.out.SaveMatchingMatePort;
import idorm.idormServer.matchingMate.entity.MatchingMate;
import idorm.idormServer.matchingMate.entity.MatePreferenceType;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingMateService implements MatchingMateUseCase {

	private final LoadMemberPort loadMemberPort;

	private final LoadMatchingInfoPort loadMatchingInfoPort;

	private final SaveMatchingMatePort saveMatchingMatePort;

	@Override
	public List<MatchingMateResponse> findFavoriteMates(final AuthResponse auth) {
		Member member = loadMemberPort.loadMember(auth.getId());
		return convertMateResponses(member.getFavoriteMates());
	}

	@Override
	public List<MatchingMateResponse> findNonFavoriteMates(final AuthResponse auth) {
		Member member = loadMemberPort.loadMember(auth.getId());
		return convertMateResponses(member.getNonFavoriteMates());
	}

	@Override
	@Transactional
	public void addMate(final AuthResponse auth, final PreferenceMateRequest request) {
		MatePreferenceType preferenceType = request.getPreferenceType();

		Member member = loadMemberPort.loadMember(auth.getId());
		Member targetMember = loadMemberPort.loadMember(request.targetMemberId());

		member.validateMatchingStatement();
		targetMember.validateMatchingStatement();

		MatchingMate mate =
			preferenceType.isFavorite() ? MatchingMate.favoriteFrom(member, targetMember.getMatchingInfo()) :
				MatchingMate.nonFavoriteFrom(member, targetMember.getMatchingInfo());
		saveMatchingMatePort.save(mate);
		member.addMate(mate);
	}

	@Override
	@Transactional
	public void deleteMate(final AuthResponse auth, final PreferenceMateRequest request) {
		MatePreferenceType preferenceType = request.getPreferenceType();

		Member member = loadMemberPort.loadMember(auth.getId());
		Member targetMember = loadMemberPort.loadMember(request.targetMemberId());

		MatchingMate mate =
			preferenceType.isFavorite() ? MatchingMate.favoriteFrom(member, targetMember.getMatchingInfo()) :
				MatchingMate.nonFavoriteFrom(member, targetMember.getMatchingInfo());
		member.deleteMate(mate);
	}

	@Override
	public List<MatchingMateResponse> findMates(final AuthResponse auth) {
		Member member = loadMemberPort.loadMember(auth.getId());
		MatchingInfo matchingInfo = loadMatchingInfoPort.load(member.getId());
		matchingInfo.validateStatement();

		List<MatchingInfo> matchingInfos = loadMatchingInfoPort.loadByBasicConditions(matchingInfo);
		return removeNonFavoriteMates(member, matchingInfos);
	}

	@Override
	public List<MatchingMateResponse> findFilteredMates(final AuthResponse auth,
		final MatchingMateFilterRequest request) {

		Member member = loadMemberPort.loadMember(auth.getId());
		MatchingInfo matchingInfo = loadMatchingInfoPort.load(member.getId());
		matchingInfo.validateStatement();

		List<MatchingInfo> matchingInfos = loadMatchingInfoPort.loadBySpecialConditions(matchingInfo, request);
		return removeNonFavoriteMates(member, matchingInfos);
	}

	private List<MatchingMateResponse> convertMateResponses(final List<MatchingMate> mates) {
		List<MatchingMateResponse> responses = mates.stream()
			.map(MatchingMate::getMatchingInfoForTarget)
			.map(MatchingMateResponse::from)
			.toList();
		return responses;
	}

	private List<MatchingMateResponse> removeNonFavoriteMates(final Member loginMember,
		final List<MatchingInfo> matchingInfos) {
		List<MatchingMateResponse> responses = matchingInfos.stream()
			.filter(loginMember::isNotNonFavoriteMate)
			.map(MatchingMateResponse::from)
			.toList();
		return responses;
	}
}
