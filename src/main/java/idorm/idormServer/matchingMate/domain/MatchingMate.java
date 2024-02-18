package idorm.idormServer.matchingMate.domain;

import java.util.List;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchingMate {

	private Long id;
	private Member member;
	private Member targetMember;
	private MatePreferenceType preferenceType;

	private MatchingMate(final Member loginMember, final Member targetMember, final MatePreferenceType preferenceType) {
		validateConstructor(loginMember, targetMember);
		this.member = loginMember;
		this.targetMember = targetMember;
		this.preferenceType = preferenceType;
	}

	public static MatchingMate of(final Member loginMember,
		final Member targetMember,
		final MatePreferenceType preferenceType) {
		return new MatchingMate(loginMember, targetMember, preferenceType);
	}

	private static void validateConstructor(Member loginMember, Member targetMember) {
		Validator.validateNotNull(List.of(loginMember, targetMember));
	}

	public static MatchingMate forMapper(final Long id,
		final Member loginMember,
		final Member targetMember,
		final MatePreferenceType preferenceType) {
		return new MatchingMate(id, loginMember, targetMember, preferenceType);
	}
}