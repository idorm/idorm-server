package idorm.idormServer.member.adapter.out.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordEmbeddedEntity {

    @Column(name = "password", nullable = false)
    private String value;

    PasswordEmbeddedEntity(final String value) {
        this.value = value;
    }
}