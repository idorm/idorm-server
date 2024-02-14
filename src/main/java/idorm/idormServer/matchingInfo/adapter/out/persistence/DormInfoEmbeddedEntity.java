package idorm.idormServer.matchingInfo.adapter.out.persistence;

import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DormInfoEmbeddedEntity {

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
    DormInfoEmbeddedEntity(final DormCategory dormCategory, final JoinPeriod joinPeriod, final Gender gender) {
        this.dormCategory = dormCategory;
        this.joinPeriod = joinPeriod;
        this.gender = gender;
    }
}