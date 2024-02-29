package idorm.idormServer.matchingMate.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingMate {

	@Id
	@Column(name = "matching_mate_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(columnDefinition = "ENUM('FAVORITE', 'NONFAVORITE')")
	private MatePreferenceType preferenceType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member owner;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "matching_info_id")
	private MatchingInfo matchingInfoForTarget;

	private MatchingMate(final MatePreferenceType preferenceType, final Member owner, final MatchingInfo matchingInfo) {
		validateConstructor(owner, matchingInfo);
		this.preferenceType = preferenceType;
		this.owner = owner;
		this.matchingInfoForTarget = matchingInfo;

		owner.addMate(this);
		matchingInfoForTarget.addMatchingMate(this);
	}

	public static MatchingMate favoriteFrom(final Member owner, final MatchingInfo matchingInfo) {
		return new MatchingMate(MatePreferenceType.FAVORITE, owner, matchingInfo);
	}

	public static MatchingMate nonFavoriteFrom(final Member owner, final MatchingInfo matchingInfo) {
		return new MatchingMate(MatePreferenceType.NONFAVORITE, owner, matchingInfo);
	}

	private void validateConstructor(final Member member, final MatchingInfo matchingInfo) {
		Validator.validateNotNull(List.of(member, matchingInfo));

	}

	public boolean isPublic() {
		return matchingInfoForTarget.getIsPublic();
	}

	public boolean isFavorite() {
		return preferenceType.equals(MatePreferenceType.FAVORITE);
	}

	public boolean isNonFavorite() {
		return preferenceType.equals(MatePreferenceType.NONFAVORITE);
	}

	public boolean isNonFavorite(final MatchingInfo matchingInfo) {
		return this.matchingInfoForTarget.equals(matchingInfo) && isNonFavorite();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		MatchingMate mate = (MatchingMate)object;
		return Objects.equals(owner, mate.owner) && Objects.equals(matchingInfoForTarget,
			mate.matchingInfoForTarget);
	}

	@Override
	public int hashCode() {
		return Objects.hash(owner, matchingInfoForTarget);
	}
}