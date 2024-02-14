package idorm.idormServer.email.adapter.out.persistence;

import static idorm.idormServer.email.domain.VerificationCode.CODE_LENGTH;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationCodeEmbeddedEntity {

    @Column(name = "code", nullable = false, length = CODE_LENGTH)
    private String value;

    VerificationCodeEmbeddedEntity(final String value) {
        this.value = value;
    }
}