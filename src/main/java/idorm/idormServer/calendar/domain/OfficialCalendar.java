package idorm.idormServer.calendar.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

// TODO: 공식 일정 도메인 관련 전체 클래스 리팩 대상
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OfficialCalendar {

	private static final String CRAWLING_FAIL_VALUE = "fail";
	private static final LocalDate CRAWLING_FAIL_DATE = LocalDate.of(2020, 1, 1);
	private static final String BLANK_TITLE_MESSAGE = "크롤링 실패 : 게시글 제목";
	private static final String BLANK_INU_POST_ID_MESSAGE = "크롤링 실패 : 게시글 ID";

	public static final int MAX_INU_POST_ID_LENGTH = 10;

	private Long id;
	private Boolean isDorm1Yn;
	private Boolean isDorm2Yn;
	private Boolean isDorm3Yn;
	private Period period;
	private Title title;
	private Boolean isPublic;
	private String inuPostId;
	private String inuPostUrl;
	private LocalDate inuPostCreatedAt;
	private Boolean isDeleted;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static OfficialCalendar forMapper(final Long id,
		final Boolean isDorm1Yn,
		final Boolean isDorm2Yn,
		final Boolean isDorm3Yn,
		final Period period,
		final Title title,
		final Boolean isPublic,
		final String inuPostId,
		final String inuPostUrl,
		final LocalDate inuPostCreatedAt,
		final Boolean isDeleted,
		final LocalDateTime createdAt,
		final LocalDateTime updatedAt) {

		return new OfficialCalendar(id, isDorm1Yn, isDorm2Yn, isDorm3Yn, period, title,
			isPublic, inuPostId, inuPostUrl, inuPostCreatedAt, isDeleted, createdAt, updatedAt);
	}

	private static boolean isBlank(String value) {
		return Objects.isNull(value) || value.isBlank();
	}

	private static boolean isNull(Object value) {
		return Objects.isNull(value);
	}

	public void update(Boolean isDorm1Yn,
		Boolean isDorm2Yn,
		Boolean isDorm3Yn,
		LocalDate startDate,
		LocalDate endDate,
		String title,
		Boolean isPublic) {
		validate(List.of(isDorm1Yn, isDorm2Yn, isDorm3Yn, isPublic));
		this.isDorm1Yn = isDorm1Yn;
		this.isDorm2Yn = isDorm2Yn;
		this.isDorm3Yn = isDorm3Yn;
		this.period = new Period(startDate, endDate);

		if (!this.title.equals(title)) {
			this.title = Title.officialCalendar(title);
		}
	}

	private void validate(List<Object> values) {
		Validator.validateNotNull(values);
	}

	public void delete() {
		this.isDeleted = true;
	}
}