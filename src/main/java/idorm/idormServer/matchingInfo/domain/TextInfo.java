package idorm.idormServer.matchingInfo.domain;

import static idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode.*;

import java.util.List;

import idorm.idormServer.common.util.Validator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TextInfo {

	public static final int MAX_WAKE_UP_TIME_SIZE = 30;
	public static final int MAX_CLEAN_UP_STATUS_SIZE = 30;
	public static final int MAX_SHOWER_TIME_SIZE = 30;
	public static final int MAX_WISH_TEXT_SIZE = 150;

	private static final String MBTI_REGEX = "^[EI][SN][TF][JP]$";

	private String wakeUpTime;
	private String cleanUpStatus;
	private String showerTime;
	private String wishText;
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

	public static TextInfo forMapper(final String wakeUpTime,
		final String cleanUpStatus,
		final String showerTime,
		final String wishText,
		final String mbti) {

		return new TextInfo(wakeUpTime, cleanUpStatus, showerTime, wishText, mbti);
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