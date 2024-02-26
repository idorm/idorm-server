package idorm.idormServer.matchingMate.application.port.in.dto;

import idorm.idormServer.matchingInfo.entity.MatchingInfo;

public record MatchingMateResponse(
	Long memberId,
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
	String wishText
) {

	public static MatchingMateResponse from(final MatchingInfo matchingInfo) {

		return new MatchingMateResponse(matchingInfo.getMember().getId(),
			matchingInfo.getId(),
			matchingInfo.getDormInfo().getDormCategory().name(),
			matchingInfo.getDormInfo().getJoinPeriod().name(),
			matchingInfo.getDormInfo().getGender().name(),
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
			matchingInfo.getTextInfo().getWishText());
	}
}
