package idorm.idormServer.matchingInfo.adapter.out.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AgeEmbeddedEntity {

    @Column(name = "age", nullable = false)
    private Integer value;

    AgeEmbeddedEntity(final Integer value) {
        this.value = value;
    }
}
