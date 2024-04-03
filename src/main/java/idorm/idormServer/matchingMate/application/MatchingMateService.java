package idorm.idormServer.matchingMate.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.common.exception.InvalidTargetSelfException;
import idorm.idormServer.matchingInfo.application.port.out.LoadMatchingInfoPort;
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
	public Page<MatchingMateResponse> findFavoriteMates(final AuthResponse auth, final Pageable pageable) {
		Member member = loadMemberPort.loadMember(auth.getId());
		return loadMatchingInfoPort.loadByMatePreference(member, MatePreferenceType.FAVORITE, pageable);
	}

	@Override
	public Page<MatchingMateResponse> findNonFavoriteMates(final AuthResponse auth, final Pageable pageable) {
		Member member = loadMemberPort.loadMember(auth.getId());
		return loadMatchingInfoPort.loadByMatePreference(member, MatePreferenceType.NONFAVORITE, pageable);
	}

	@Override
	@Transactional
	public void addMate(final AuthResponse auth, final PreferenceMateRequest request) {
		MatePreferenceType preferenceType = request.getPreferenceType();

		Member member = loadMemberPort.loadMember(auth.getId());
		Member targetMember = loadMemberPort.loadMember(request.targetMemberId());

		validateMatchingStatement(member, targetMember);

		MatchingMate mate =
			preferenceType.isFavorite() ? MatchingMate.favoriteFrom(member, targetMember.getMatchingInfo()) :
				MatchingMate.nonFavoriteFrom(member, targetMember.getMatchingInfo());

		saveMatchingMatePort.save(mate);
	}

	@Override
	@Transactional
	public void deleteMate(final AuthResponse auth, final PreferenceMateRequest request) {
		Member member = loadMemberPort.loadMember(auth.getId());
		Member targetMember = loadMemberPort.loadMember(request.targetMemberId());

		member.deleteMate(targetMember.getMatchingInfo(), request.getPreferenceType());
	}

	@Override
	public Page<MatchingMateResponse> findMates(final AuthResponse auth, final Pageable pageable) {
		Member member = loadMemberPort.loadMember(auth.getId());
		return loadMatchingInfoPort.loadByBasicConditions(member, pageable);
	}

	@Override
	public Page<MatchingMateResponse> findFilteredMates(final AuthResponse auth,
		final MatchingMateFilterRequest request, final Pageable pageable) {

		Member member = loadMemberPort.loadMember(auth.getId());
		return loadMatchingInfoPort.loadBySpecialConditions(member, request, pageable);
	}

	private void validateMatchingStatement(final Member owner, final Member target) {
		if (owner.equals(target)) {
			throw new InvalidTargetSelfException();
		}
		owner.validateMatchingStatement();
		target.validateMatchingStatement();
	}
}
