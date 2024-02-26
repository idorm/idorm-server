package idorm.idormServer.matchingInfo.entity;

import static idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode.*;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingInfo {

	private static final int MAX_URL_SIZE = 100;

	@Id
	@Column(name = "matching_info_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(mappedBy = "matchingInfo")
	private Member member;

	@Embedded
	private DormInfo dormInfo;

	@Embedded
	private PreferenceInfo preferenceInfo;

	@Embedded
	private TextInfo textInfo;

	@Column(nullable = false, name = "shared_url", length = MAX_URL_SIZE)
	private String sharedURL;

	@Column(nullable = false)
	private Boolean isPublic;

	@Builder
	public MatchingInfo(final DormInfo dormInfo,
		final PreferenceInfo preferenceInfo,
		final TextInfo textInfo,
		final String sharedURL,
		final Member member) {
		validateConstructor(dormInfo, preferenceInfo, textInfo, sharedURL, member);
		this.dormInfo = dormInfo;
		this.preferenceInfo = preferenceInfo;
		this.textInfo = textInfo;
		this.sharedURL = sharedURL;
		this.isPublic = false;
		this.member = member;
	}

	private void validateConstructor(final DormInfo dormInfo,
		final PreferenceInfo preferenceInfo,
		final TextInfo textInfo,
		final String sharedURL,
		final Member member) {
		Validator.validateNotNull(List.of(dormInfo, preferenceInfo, textInfo, member));
		Validator.validateMaxLength(sharedURL, MAX_URL_SIZE, INVALID_OPENKAKAOLINK_LENGTH);
	}

	public void editAll(final DormInfo dormInfo, final PreferenceInfo preferenceInfo, final TextInfo textInfo,
		final String sharedURL) {
		this.dormInfo = dormInfo;
		this.preferenceInfo = preferenceInfo;
		this.textInfo = textInfo;
		this.sharedURL = sharedURL;
	}

	public void editVisibility(final Boolean isPublic) {
		this.isPublic = isPublic;
	}
}