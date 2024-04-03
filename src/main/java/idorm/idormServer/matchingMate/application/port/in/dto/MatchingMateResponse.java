package idorm.idormServer.matchingMate.application.port.in.dto;

import com.querydsl.core.annotations.QueryProjection;

import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import lombok.Data;

@Data
public class MatchingMateResponse {

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
	}
}