package idorm.idormServer.matchingInfo.application.port.in.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.matchingInfo.entity.DormInfo;
import idorm.idormServer.matchingInfo.entity.Gender;
import idorm.idormServer.matchingInfo.entity.JoinPeriod;
import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import idorm.idormServer.matchingInfo.entity.PreferenceInfo;
import idorm.idormServer.matchingInfo.entity.TextInfo;
import idorm.idormServer.member.entity.Member;

public record MatchingInfoRequest(
	@NotBlank(message = "기숙사 분류를 입력해 주세요.")
	String dormCategory,

	@NotBlank(message = "입사기간을 입력해 주세요.")
	String joinPeriod,

	@NotBlank(message = "성별을 입력해주세요.")
	String gender,

	@NotNull(message = "나이 입력은 필수입니다.")
	@Range(min = 20, max = 100, message = "나이는 20~100살 이내여야 합니다.")
	Integer age,

	@NotNull(message = "코골이 여부 입력은 필수입니다.")
	Boolean isSnoring,

	@NotNull(message = "이갈이 여부 입력은 필수입니다.")
	Boolean isGrinding,

	@NotNull(message = "흡연 여부 입력은 필수입니다.")
	Boolean isSmoking,

	@NotNull(message = "실내 음식 허용 여부 입력은 필수입니다.")
	Boolean isAllowedFood,

	@NotNull(message = "이어폰 착용 의사 여부 입력은 필수입니다.")
	Boolean isWearEarphones,

	@NotBlank(message = "기상 시간 입력은 필수입니다.")
	@Size(max = 30, message = "기상 시간은 ~30자 이내여야 합니다.")
	String wakeupTime,

	@NotBlank(message = "정리정돈 상태 입력은 필수입니다.")
	@Size(max = 30, message = "정리정돈 상태는 ~30자 이내여야 합니다.")
	String cleanUpStatus,

	@NotBlank(message = "샤워 시간 입력은 필수입니다.")
	@Size(max = 30, message = "샤워시간은 ~30자 이내여야 합니다.")
	String showerTime,

	@NotBlank(message = "오픈 채팅 링크 입력은 필수입니다.")
	@Size(max = 100, message = "오픈채팅 링크는 ~100자 이내여야 합니다.")
	String openKakaoLink,

	@Size(max = 4, message = "mbti는 ~4자 여야 합니다.")
	String mbti,

	@Size(max = 150, message = "하고싶은 말은 ~150자 이내여야 합니다.")
	String wishText
) {

	public MatchingInfo from(final Member member) {

		return MatchingInfo.builder()
			.member(member)
			.dormInfo(dormInfoOf())
			.preferenceInfo(preferenceInfoOf())
			.textInfo(textInfoOf())
			.sharedURL(openKakaoLink)
			.build();
	}

	public void editAll(MatchingInfo matchingInfo) {
		matchingInfo.editAll(dormInfoOf(), preferenceInfoOf(), textInfoOf(), openKakaoLink);
	}

	private DormInfo dormInfoOf() {
		return new DormInfo(DormCategory.from(dormCategory), JoinPeriod.from(joinPeriod), Gender.from(gender));
	}

	private PreferenceInfo preferenceInfoOf() {
		return new PreferenceInfo(isSnoring, isGrinding, isSmoking, isAllowedFood, isWearEarphones, age);
	}

	private TextInfo textInfoOf() {
		return new TextInfo(wakeupTime, cleanUpStatus, showerTime, wishText, mbti);
	}
}