package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.common.util.Validator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferenceInfo {

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

    @Embedded
    private Age age;

    @Builder
    public PreferenceInfo(Boolean isSnoring, Boolean isGrinding, Boolean isSmoking, Boolean isAllowedFood,
                          Boolean isWearEarphones, Age age) {
        validate(isSnoring, isGrinding, isSmoking, isAllowedFood, isWearEarphones);
        this.isSnoring = isSnoring;
        this.isGrinding = isGrinding;
        this.isSmoking = isSmoking;
        this.isAllowedFood = isAllowedFood;
        this.isWearEarphones = isWearEarphones;
        this.age = age;
    }

    private void validate(Boolean isSnoring, Boolean isGrinding, Boolean isSmoking, Boolean isAllowedFood,
                          Boolean isWearEarphones) {
        Validator.validateNotNull(List.of(isSnoring, isGrinding, isSmoking, isAllowedFood, isWearEarphones));
    }
}