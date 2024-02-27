package idorm.idormServer.matchingInfo.entity;

import static idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.matchingInfo.adapter.out.exception.IllegalStatementMatchingInfoNonPublicException;
import idorm.idormServer.matchingMate.entity.MatchingMate;
import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MatchingInfo {

	private static final int MAX_URL_SIZE = 100;

	@Id
	@Column(name = "matching_info_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	@OneToOne(mappedBy = "matchingInfo", fetch = FetchType.LAZY)
	private Member member;

	@OneToMany(mappedBy = "matchingInfoForTarget", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<MatchingMate> matchingMates = new ArrayList<>();

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

		member.updateMatchingInfo(this);
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

	public void addMatchingMate(final MatchingMate mate) {
		this.matchingMates.add(mate);
	}

	public void editVisibility(final Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public void validateStatement() {
		if (!isPublic) {
			throw new IllegalStatementMatchingInfoNonPublicException();
		}
	}
}