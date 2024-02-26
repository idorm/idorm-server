package idorm.idormServer.matchingInfo.application.port.in.dto;

import idorm.idormServer.matchingInfo.entity.MatchingInfo;

public record MatchingInfoResponse(
	Long matchingInfoId,
	String dormCategory,
	String joinPeriod,
	String gender,
	Integer age,
	Boolean isSnoring,
	Boolean isGrinding,
	Boolean isSmoking,
	Boolean isAllowedFood,
	Boolean isWearEarphones,
	String wakeUpTime,
	String cleanUpStatus,
	String showerTime,
	String openKakaoLink,
	String mbti,
	String wishText,
	Boolean isMatchingInfoPublic
) {

	public static MatchingInfoResponse from(final MatchingInfo matchingInfo) {

		return new MatchingInfoResponse(matchingInfo.getId(),
			matchingInfo.getDormInfo().getDormCategory().toString(),
			matchingInfo.getDormInfo().getJoinPeriod().toString(),
			matchingInfo.getDormInfo().getGender().toString(),
			matchingInfo.getPreferenceInfo().getAge(),
			matchingInfo.getPreferenceInfo().getIsSnoring(),
			matchingInfo.getPreferenceInfo().getIsGrinding(),
			matchingInfo.getPreferenceInfo().getIsSmoking(),
			matchingInfo.getPreferenceInfo().getIsAllowedFood(),
			matchingInfo.getPreferenceInfo().getIsWearEarphones(),
			matchingInfo.getTextInfo().getWakeUpTime(),
			matchingInfo.getTextInfo().getCleanUpStatus(),
			matchingInfo.getTextInfo().getShowerTime(),
			matchingInfo.getSharedURL(),
			matchingInfo.getTextInfo().getMbti(),
			matchingInfo.getTextInfo().getWishText(),
			matchingInfo.getIsPublic());
	}
}
