package idorm.idormServer.matchingInfo.domain;

import static idorm.idormServer.common.exception.ExceptionCode.CLEANUPSTATUS_LENGTH_INVALID;
import static idorm.idormServer.common.exception.ExceptionCode.MBTI_CHARACTER_INVALID;
import static idorm.idormServer.common.exception.ExceptionCode.SHOWERTIME_LENGTH_INVALID;
import static idorm.idormServer.common.exception.ExceptionCode.WAKEUPTIME_LENGTH_INVALID;
import static idorm.idormServer.common.exception.ExceptionCode.WISHTEXT_LENGTH_INVALID;

import idorm.idormServer.common.util.Validator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextInfo {

    private static final int MAX_WAKE_UP_TIME_SIZE = 30;
    private static final int MAX_CLEAN_UP_STATUS_SIZE = 30;
    private static final int MAX_SHOWER_TIME_SIZE = 30;
    private static final int MAX_WISH_TEXT_SIZE = 150;

    private static final String MBTI_REGEX = "^[EI][SN][TF][JP]$";

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
    public TextInfo(final String wakeUpTime,
                    final String cleanUpStatus,
                    final String showerTime,
                    final String wishText,
                    final String mbti) {
        validate(wakeUpTime, cleanUpStatus, showerTime, wishText, mbti);
        this.wakeUpTime = wakeUpTime;
        this.cleanUpStatus = cleanUpStatus;
        this.showerTime = showerTime;
        this.wishText = wishText;
        this.mbti = mbti;
    }

    private void validate(final String wakeUpTime,
                          final String cleanUpStatus,
                          final String showerTime,
                          final String wishText,
                          final String mbti) {
        Validator.validateNotBlank(List.of(wakeUpTime, cleanUpStatus, showerTime));
        Validator.validateMaxLength(wakeUpTime, MAX_WAKE_UP_TIME_SIZE, WAKEUPTIME_LENGTH_INVALID);
        Validator.validateMaxLength(cleanUpStatus, MAX_CLEAN_UP_STATUS_SIZE, CLEANUPSTATUS_LENGTH_INVALID);
        Validator.validateMaxLength(showerTime, MAX_SHOWER_TIME_SIZE, SHOWERTIME_LENGTH_INVALID);

        Validator.validateFormat(mbti, MBTI_REGEX, MBTI_CHARACTER_INVALID);
        Validator.validateMaxLength(wishText, MAX_WISH_TEXT_SIZE, WISHTEXT_LENGTH_INVALID);
    }
}
