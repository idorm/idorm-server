package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.common.util.Validator;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PreferenceInfo {

    private Boolean isSnoring;
    private Boolean isGrinding;
    private Boolean isSmoking;
    private Boolean isAllowedFood;
    private Boolean isWearEarphones;
    private Age age;

    @Builder
    public PreferenceInfo(final Boolean isSnoring,
                          final Boolean isGrinding,
                          final Boolean isSmoking,
                          final Boolean isAllowedFood,
                          final Boolean isWearEarphones,
                          final Age age) {
        validate(isSnoring, isGrinding, isSmoking, isAllowedFood, isWearEarphones);
        this.isSnoring = isSnoring;
        this.isGrinding = isGrinding;
        this.isSmoking = isSmoking;
        this.isAllowedFood = isAllowedFood;
        this.isWearEarphones = isWearEarphones;
        this.age = age;
    }

    public static PreferenceInfo forMapper(final Boolean isSnoring,
                                           final Boolean isGrinding,
                                           final Boolean isSmoking,
                                           final Boolean isAllowedFood,
                                           final Boolean isWearEarphones,
                                           final Age age) {
        return new PreferenceInfo(isSnoring, isGrinding, isSmoking, isAllowedFood, isWearEarphones, age);
    }

    private void validate(Boolean isSnoring, Boolean isGrinding, Boolean isSmoking, Boolean isAllowedFood,
                          Boolean isWearEarphones) {
        Validator.validateNotNull(List.of(isSnoring, isGrinding, isSmoking, isAllowedFood, isWearEarphones));
    }
}