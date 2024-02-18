package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchingInfo {

	private Long id;
	private Member member;
	private DormInfo dormInfo;
	private PreferenceInfo preferenceInfo;
	private TextInfo textInfo;
	private SharedURL sharedURL;
	private Boolean isPublic;

	@Builder
	public MatchingInfo(DormInfo dormInfo,
		PreferenceInfo preferenceInfo,
		TextInfo textInfo,
		SharedURL sharedURL,
		Member member) {

		this.dormInfo = dormInfo;
		this.preferenceInfo = preferenceInfo;
		this.textInfo = textInfo;
		this.sharedURL = sharedURL;
		this.isPublic = false;
		this.member = member;
	}

	public static MatchingInfo forMapper(final Long id,
		final Member member,
		final DormInfo dormInfo,
		final PreferenceInfo preferenceInfo,
		final TextInfo textInfo,
		final SharedURL sharedURL,
		final Boolean isPublic) {

		return new MatchingInfo(id, member, dormInfo, preferenceInfo, textInfo, sharedURL, isPublic);
	}

	public void editAll(final DormInfo dormInfo,
		final PreferenceInfo preferenceInfo,
		final TextInfo textInfo,
		final SharedURL sharedURL) {

		this.dormInfo = dormInfo;
		this.preferenceInfo = preferenceInfo;
		this.textInfo = textInfo;
		this.sharedURL = sharedURL;
	}

	public void editVisibility(final Boolean isPublic) {
		this.isPublic = isPublic;
	}
}