package idorm.idormServer.matchingMate.application.port.in.dto;

import com.querydsl.core.annotations.QueryProjection;

import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import lombok.Data;

@Data
public class MatchingMateResponse {

	// private Long memberId;
	// private Long matchingInfoId;
	// private DormInfo dormInfo;
	// private PreferenceInfo preferenceInfo;
	// private TextInfo textInfo;
	// private String openKakaoLink;
	//
	// @QueryProjection
	// public MatchingMateResponse(Long memberId, Long matchingInfoId, DormInfo dormInfo, PreferenceInfo preferenceInfo,
	// 	TextInfo textInfo, String openKakaoLink) {
	// 	this.memberId = memberId;
	// 	this.matchingInfoId = matchingInfoId;
	// 	this.dormInfo = dormInfo;
	// 	this.preferenceInfo = preferenceInfo;
	// 	this.textInfo = textInfo;
	// 	this.openKakaoLink = openKakaoLink;
	// }

	private Long memberId;
	private Long matchingInfoId;
	private String dormCategory;
	private String joinPeriod;
	private String gender;
	private Integer age;
	private Boolean isSnoring;
	private Boolean isGrinding;
	private Boolean isSmoking;
	private Boolean isAllowedFood;
	private Boolean isWearEarphones;
	private String wakeUpTime;
	private String cleanUpStatus;
	private String showerTime;
	private String openKakaoLink;
	private String mbti;
	private String wishText;

	// @QueryProjection
	// public MatchingMateResponse(Long memberId, Long matchingInfoId, String dormCategory, String joinPeriod,
	// 	String gender,
	// 	Integer age, Boolean isSnoring, Boolean isGrinding, Boolean isSmoking, Boolean isAllowedFood,
	// 	Boolean isWearEarphones, String wakeUpTime, String cleanUpStatus, String showerTime, String openKakaoLink,
	// 	String mbti, String wishText) {
	// 	this.memberId = memberId;
	// 	this.matchingInfoId = matchingInfoId;
	// 	this.dormCategory = dormCategory;
	// 	this.joinPeriod = joinPeriod;
	// 	this.gender = gender;
	// 	this.age = age;
	// 	this.isSnoring = isSnoring;
	// 	this.isGrinding = isGrinding;
	// 	this.isSmoking = isSmoking;
	// 	this.isAllowedFood = isAllowedFood;
	// 	this.isWearEarphones = isWearEarphones;
	// 	this.wakeUpTime = wakeUpTime;
	// 	this.cleanUpStatus = cleanUpStatus;
	// 	this.showerTime = showerTime;
	// 	this.openKakaoLink = openKakaoLink;
	// 	this.mbti = mbti;
	// 	this.wishText = wishText;
	// }

	@QueryProjection
	public MatchingMateResponse(MatchingInfo matchingInfo, Long memberId) {

		this.memberId = memberId;
		this.matchingInfoId = matchingInfo.getId();
		this.dormCategory = matchingInfo.getDormInfo().getDormCategory().name();
		this.joinPeriod = matchingInfo.getDormInfo().getJoinPeriod().name();
		this.gender = matchingInfo.getDormInfo().getGender().name();
		this.age = matchingInfo.getPreferenceInfo().getAge();
		this.isSnoring = matchingInfo.getPreferenceInfo().getIsSnoring();
		this.isGrinding = matchingInfo.getPreferenceInfo().getIsGrinding();
		this.isSmoking = matchingInfo.getPreferenceInfo().getIsSmoking();
		this.isAllowedFood = matchingInfo.getPreferenceInfo().getIsAllowedFood();
		this.isWearEarphones = matchingInfo.getPreferenceInfo().getIsWearEarphones();
		this.wakeUpTime = matchingInfo.getTextInfo().getWakeUpTime();
		this.cleanUpStatus = matchingInfo.getTextInfo().getCleanUpStatus();
		this.showerTime = matchingInfo.getTextInfo().getShowerTime();
		this.openKakaoLink = matchingInfo.getSharedURL();
		this.mbti = matchingInfo.getTextInfo().getMbti();
		this.wishText = matchingInfo.getTextInfo().getWishText();

		// return new MatchingMateResponse(
		// 	.memberId(memberId)
		// 	.matchingInfoId(matchingInfo.getId())
		// 	.dormCategory(matchingInfo.getDormInfo().getDormCategory().name())
		// 	.joinPeriod(matchingInfo.getDormInfo().getJoinPeriod().name())
		// 	.gender(matchingInfo.getDormInfo().getGender().name())
		// 	.age(matchingInfo.getPreferenceInfo().getAge())
		// 	.isSnoring(matchingInfo.getPreferenceInfo().getIsSnoring())
		// 	.isGrinding(matchingInfo.getPreferenceInfo().getIsGrinding())
		// 	.isSmoking(matchingInfo.getPreferenceInfo().getIsSmoking())
		// 	.isAllowedFood(matchingInfo.getPreferenceInfo().getIsAllowedFood())
		// 	.isWearEarphones(matchingInfo.getPreferenceInfo().getIsWearEarphones())
		// 	.wakeUpTime(matchingInfo.getTextInfo().getWakeUpTime())
		// 	.cleanUpStatus(matchingInfo.getTextInfo().getCleanUpStatus())
		// 	.showerTime(matchingInfo.getTextInfo().getShowerTime())
		// 	.openKakaoLink(matchingInfo.getSharedURL())
		// 	.mbti(matchingInfo.getTextInfo().getMbti())
		// 	.wishText(matchingInfo.getTextInfo().getWishText()))

	}

}