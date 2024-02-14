package idorm.idormServer.member.adapter.out.persistence;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NicknameEmbeddedEntity {

    @Column(name = "nickname", nullable = false, unique = true)
    private String value;

    @CreatedDate
    @Column(name = "nickname_created_at", updatable = false)
    private LocalDateTime createdAt;

    NicknameEmbeddedEntity(final String value) {
        this.value = value;
        this.createdAt = LocalDateTime.now();
    }
}