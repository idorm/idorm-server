package idorm.idormServer.matchingInfo.entity;

import static idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode.*;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextInfo {

	public static final int MAX_WAKE_UP_TIME_SIZE = 30;
	public static final int MAX_CLEAN_UP_STATUS_SIZE = 30;
	public static final int MAX_SHOWER_TIME_SIZE = 30;
	public static final int MAX_WISH_TEXT_SIZE = 150;

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

		validate(wakeUpTime, cleanUpStatus, showerTime, wishText, mbti.toUpperCase());
		this.wakeUpTime = wakeUpTime;
		this.cleanUpStatus = cleanUpStatus;
		this.showerTime = showerTime;
		this.wishText = wishText;
		this.mbti = mbti.toUpperCase();
	}

	private void validate(final String wakeUpTime,
		final String cleanUpStatus,
		final String showerTime,
		final String wishText,
		final String mbti) {

		Validator.validateNotBlank(List.of(wakeUpTime, cleanUpStatus, showerTime));

		Validator.validateMaxLength(wakeUpTime, MAX_WAKE_UP_TIME_SIZE, INVALID_WAKEUPTIME_LENGTH);
		Validator.validateMaxLength(cleanUpStatus, MAX_CLEAN_UP_STATUS_SIZE, INVALID_CLEENUP_STATUS_LENGTH);
		Validator.validateMaxLength(showerTime, MAX_SHOWER_TIME_SIZE, INVALID_SHOWERTIME_LENGTH);
		Validator.validateMaxLength(wishText, MAX_WISH_TEXT_SIZE, INVALID_WISHTEXT_LENGTH);

		Validator.validateFormat(mbti, MBTI_REGEX, INVALID_MBTI_CHARACTER);
	}
}