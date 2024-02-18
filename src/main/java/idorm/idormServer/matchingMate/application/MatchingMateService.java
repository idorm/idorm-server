package idorm.idormServer.matchingMate.application;

import static idorm.idormServer.matchingMate.domain.MatePreferenceType.*;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.matchingInfo.adapter.out.exception.IllegalStatementMatchingInfoNonPublicException;
import idorm.idormServer.matchingInfo.application.port.out.LoadMatchingInfoPort;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingMate.application.port.in.MatchingMateUseCase;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.application.port.out.SaveMatchingMatePort;
import idorm.idormServer.matchingMate.domain.MatchingMate;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.domain.Member;
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
		validateMatchingCondition(member);

		Set<MatchingMate> favoriteMates = member.getFavoriteMates();

		List<MatchingMateResponse> responses = favoriteMates.stream()
			.map(mate -> loadMatchingInfoPort.load(mate.getTargetMember().getId()))
			.filter(MatchingInfo::getIsPublic)
			.map(MatchingMateResponse::from)
			.toList();
		return responses;
	}

	@Override
	public List<MatchingMateResponse> findNonFavoriteMates(final AuthResponse auth) {
		Member member = loadMemberPort.loadMember(auth.getId());
		validateMatchingCondition(member);

		Set<MatchingMate> nonFavoriteMates = member.getNonFavoriteMates();

		List<MatchingMateResponse> responses = nonFavoriteMates.stream()
			.map(mate -> loadMatchingInfoPort.load(mate.getTargetMember().getId()))
			.filter(MatchingInfo::getIsPublic)
			.map(MatchingMateResponse::from)
			.toList();
		return responses;
	}

	@Override
	@Transactional
	public void addFavoriteMate(final AuthResponse auth, final Long targetMemberId) {
		Member member = loadMemberPort.loadMember(auth.getId());
		Member targetMember = loadMemberPort.loadMember(targetMemberId);

		validateMatchingCondition(member);
		validateMatchingCondition(targetMember);

		MatchingMate matchingMate = MatchingMate.of(member, targetMember, FAVORITE);
		saveMatchingMatePort.save(matchingMate);
		member.addFavoriteMate(matchingMate);
	}

	@Override
	@Transactional
	public void addNonFavoriteMate(final AuthResponse auth, final Long targetMemberId) {
		Member member = loadMemberPort.loadMember(auth.getId());
		Member targetMember = loadMemberPort.loadMember(targetMemberId);

		validateMatchingCondition(member);
		validateMatchingCondition(targetMember);

		MatchingMate matchingMate = MatchingMate.of(member, targetMember, NONFAVORITE);
		saveMatchingMatePort.save(matchingMate);
		member.addNonFavoriteMate(matchingMate);
	}

	@Override
	@Transactional
	public void deleteFavoriteMate(final AuthResponse auth, final Long targetMemberId) {
		Member member = loadMemberPort.loadMember(auth.getId());
		Member targetMember = loadMemberPort.loadMember(targetMemberId);

		MatchingMate matchingMate = MatchingMate.of(member, targetMember, FAVORITE);
		saveMatchingMatePort.save(matchingMate);
		member.deleteFavoriteMate(matchingMate);
	}

	@Override
	@Transactional
	public void deleteNonFavoriteMate(final AuthResponse auth, final Long targetMemberId) {
		Member member = loadMemberPort.loadMember(auth.getId());
		Member targetMember = loadMemberPort.loadMember(targetMemberId);

		MatchingMate matchingMate = MatchingMate.of(member, targetMember, NONFAVORITE);
		saveMatchingMatePort.save(matchingMate);
		member.deleteNonFavoriteMate(matchingMate);
	}

	@Override
	public List<MatchingMateResponse> findMates(final AuthResponse auth) {
		Member member = loadMemberPort.loadMember(auth.getId());

		MatchingInfo matchingInfo = validateMatchingCondition(member);
		List<MatchingInfo> matchingInfos = loadMatchingInfoPort.loadByBasicConditions(matchingInfo);

		return removeNonFavoriteMates(member, matchingInfos);
	}

	@Override
	public List<MatchingMateResponse> findFilteredMates(final AuthResponse auth,
		final MatchingMateFilterRequest request) {
		Member member = loadMemberPort.loadMember(auth.getId());

		MatchingInfo matchingInfo = validateMatchingCondition(member);
		List<MatchingInfo> matchingInfos = loadMatchingInfoPort.loadBySpecialConditions(matchingInfo, request);

		return removeNonFavoriteMates(member, matchingInfos);
	}

	private List<MatchingMateResponse> removeNonFavoriteMates(final Member loginMember,
		final List<MatchingInfo> matchingInfos) {
		List<MatchingMateResponse> responses = matchingInfos.stream()
			.map(info -> {
				Member owner = loadMemberPort.loadMember(info.getMember().getId());
				if (loginMember.isNonFavoriteMate(owner)) {
					return null;
				}
				return MatchingMateResponse.from(info);
			})
			.toList();
		return responses;
	}

	private MatchingInfo validateMatchingCondition(final Member loginMember) {
		MatchingInfo matchingInfo = loadMatchingInfoPort.load(loginMember.getId());
		if (!matchingInfo.getIsPublic()) {
			throw new IllegalStatementMatchingInfoNonPublicException();
		}
		return matchingInfo;
	}
}
