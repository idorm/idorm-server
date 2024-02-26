package idorm.idormServer.matchingInfo.entity;

import static idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode.*;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferenceInfo {

	private static final int MIN_AGE_SIZE = 20;
	private static final int MAX_AGE_SIZE = 100;

	@Column(nullable = false)
	private Boolean isSnoring;

	@Column(nullable = false)
	private Boolean isGrinding;

	@Column(nullable = false)
	private Boolean isSmoking;

	@Column(nullable = false)
	private Boolean isAllowedFood;

	@Column(nullable = false)
	private Boolean isWearEarphones;

	@Column(nullable = false)
	private Integer age;

	@Builder
	public PreferenceInfo(final Boolean isSnoring,
		final Boolean isGrinding,
		final Boolean isSmoking,
		final Boolean isAllowedFood,
		final Boolean isWearEarphones,
		final Integer age) {
		validate(isSnoring, isGrinding, isSmoking, isAllowedFood, isWearEarphones, age);
		this.isSnoring = isSnoring;
		this.isGrinding = isGrinding;
		this.isSmoking = isSmoking;
		this.isAllowedFood = isAllowedFood;
		this.isWearEarphones = isWearEarphones;
		this.age = age;
	}

	private void validate(Boolean isSnoring, Boolean isGrinding, Boolean isSmoking, Boolean isAllowedFood,
		Boolean isWearEarphones, Integer age) {
		Validator.validateNotNull(List.of(isSnoring, isGrinding, isSmoking, isAllowedFood, isWearEarphones, age));
		Validator.validateSize(age, MIN_AGE_SIZE, MAX_AGE_SIZE, INVALID_AGE_LENGTH);
	}
}