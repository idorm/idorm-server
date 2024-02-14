package idorm.idormServer.matchingInfo.adapter.out.persistence;

import static idorm.idormServer.matchingInfo.domain.SharedURL.MAX_URL_SIZE;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharedURLEmbeddedEntity {

    @Column(nullable = false, name = "shared_url", length = MAX_URL_SIZE)
    private String value;

    SharedURLEmbeddedEntity(final String sharedURL) {
        this.value = sharedURL;
    }
}
