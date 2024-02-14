package idorm.idormServer.matchingInfo.adapter.out.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferenceInfoEmbeddedEntity {

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
    private AgeEmbeddedEntity age;

    @Builder
    PreferenceInfoEmbeddedEntity(final Boolean isSnoring,
                                 final Boolean isGrinding,
                                 final Boolean isSmoking,
                                 final Boolean isAllowedFood,
                                 final Boolean isWearEarphones,
                                 final AgeEmbeddedEntity age) {
        this.isSnoring = isSnoring;
        this.isGrinding = isGrinding;
        this.isSmoking = isSmoking;
        this.isAllowedFood = isAllowedFood;
        this.isWearEarphones = isWearEarphones;
        this.age = age;
    }
}