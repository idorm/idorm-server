package idorm.idormServer.matchingInfo.adapter.out.persistence;

import static idorm.idormServer.matchingInfo.domain.TextInfo.MAX_CLEAN_UP_STATUS_SIZE;
import static idorm.idormServer.matchingInfo.domain.TextInfo.MAX_SHOWER_TIME_SIZE;
import static idorm.idormServer.matchingInfo.domain.TextInfo.MAX_WAKE_UP_TIME_SIZE;
import static idorm.idormServer.matchingInfo.domain.TextInfo.MAX_WISH_TEXT_SIZE;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextInfoEmbeddedEntity {

    @Column(nullable = false, length = MAX_WAKE_UP_TIME_SIZE)
    private String wakeUpTime;

    @Column(nullable = false, length = MAX_CLEAN_UP_STATUS_SIZE)
    private String cleanUpStatus;

    @Column(nullable = false, length = MAX_SHOWER_TIME_SIZE)
    private String showerTime;

    @Column(length = MAX_WISH_TEXT_SIZE)
    private String wishText;

    @Column(length = 4)
    private String mbti;

    @Builder
    TextInfoEmbeddedEntity(final String wakeUpTime,
                    final String cleanUpStatus,
                    final String showerTime,
                    final String wishText,
                    final String mbti) {
        this.wakeUpTime = wakeUpTime;
        this.cleanUpStatus = cleanUpStatus;
        this.showerTime = showerTime;
        this.wishText = wishText;
        this.mbti = mbti;
    }
}