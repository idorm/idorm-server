package idorm.idormServer.matchingInfo.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DormInfo {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DORM1', 'DORM2', 'DORM3')")
    private DormCategory dormCategory;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('WEEK16', 'WEEK24')")
    private JoinPeriod joinPeriod;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('MALE', 'FEMALE')")
    private Gender gender;

    @Builder
    public DormInfo(DormCategory dormCategory, JoinPeriod joinPeriod, Gender gender) {
        this.dormCategory = dormCategory;
        this.joinPeriod = joinPeriod;
        this.gender = gender;
    }
}
