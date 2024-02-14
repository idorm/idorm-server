package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.common.util.Validator;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DormInfo {
    private DormCategory dormCategory;
    private JoinPeriod joinPeriod;
    private Gender gender;

    @Builder
    public DormInfo(final DormCategory dormCategory, final JoinPeriod joinPeriod, final Gender gender) {
        validateConstructor(List.of(dormCategory, joinPeriod, gender));
        this.dormCategory = dormCategory;
        this.joinPeriod = joinPeriod;
        this.gender = gender;
    }

    public static DormInfo forMapper(final DormCategory dormCategory, final JoinPeriod joinPeriod, final Gender gender) {
        return new DormInfo(dormCategory, joinPeriod, gender);
    }

    private static void validateConstructor(List<Object> input) {
        Validator.validateNotNull(input);
    }
}