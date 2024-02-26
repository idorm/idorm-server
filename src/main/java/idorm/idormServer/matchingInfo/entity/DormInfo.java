package idorm.idormServer.matchingInfo.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DormInfo {

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('DORM1', 'DORM2', 'DORM3')")
	private DormCategory dormCategory;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('WEEK16', 'WEEK24')")
	private JoinPeriod joinPeriod;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('MALE', 'FEMALE')")
	private Gender gender;

	@Builder
	public DormInfo(final DormCategory dormCategory, final JoinPeriod joinPeriod, final Gender gender) {
		validateConstructor(List.of(dormCategory, joinPeriod, gender));
		this.dormCategory = dormCategory;
		this.joinPeriod = joinPeriod;
		this.gender = gender;
	}

	private static void validateConstructor(List<Object> input) {
		Validator.validateNotNull(input);
	}
}