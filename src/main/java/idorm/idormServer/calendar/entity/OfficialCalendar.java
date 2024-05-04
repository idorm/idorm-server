package idorm.idormServer.calendar.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import idorm.idormServer.common.entity.BaseTimeEntity;
import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendar extends BaseTimeEntity {

	private static final String CRAWLING_FAIL_VALUE = "fail";
	private static final LocalDate CRAWLING_FAIL_DATE = LocalDate.of(2020, 1, 1);
	private static final String BLANK_TITLE_MESSAGE = "크롤링 실패 : 게시글 제목";
	private static final String BLANK_INU_POST_ID_MESSAGE = "크롤링 실패 : 게시글 ID";
	public static final int MAX_OFFICIAL_CALENDAR_TITLE_LENGTH = 20;

	public static final int MAX_INU_POST_ID_LENGTH = 10;

	@Id
	@Column(name = "official_calendar_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 관리자 등록, 수정 가능
	private Boolean isDorm1Yn;
	private Boolean isDorm2Yn;
	private Boolean isDorm3Yn;

	private Period period;

	// 크롤링 등록, 수정 가능
	@Column(name = "title", nullable = false, length = MAX_OFFICIAL_CALENDAR_TITLE_LENGTH)
	private String title;

	@Column(nullable = false)
	private Boolean isPublic;

	// 크롤링 등록, 수정 불가
	@Column(nullable = false, length = MAX_INU_POST_ID_LENGTH)
	private String inuPostId;

	@Column(nullable = false)
	private String inuPostUrl;

	@Column(nullable = false)
	private LocalDate inuPostCreatedAt;

	@Column(nullable = false)
	private Boolean isDeleted;

	public OfficialCalendar(String inuPostId,
		String title,
		String inuPostUrl,
		LocalDate inuPostCreatedAt
	) {
		this.inuPostId = isBlank(inuPostId) ? CRAWLING_FAIL_VALUE : inuPostId;
		this.title = isBlank(title) ? CRAWLING_FAIL_VALUE : title;
		this.inuPostUrl = isBlank(inuPostUrl) ? CRAWLING_FAIL_VALUE : inuPostUrl;
		this.inuPostCreatedAt = isNull(inuPostCreatedAt) ? CRAWLING_FAIL_DATE : inuPostCreatedAt;
		this.isPublic = false;
		this.isDeleted = false;
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
		String title) {
		validate(List.of(isDorm1Yn, isDorm2Yn, isDorm3Yn, isPublic));
		this.isDorm1Yn = isDorm1Yn;
		this.isDorm2Yn = isDorm2Yn;
		this.isDorm3Yn = isDorm3Yn;
		this.period = new Period(startDate, endDate);
		this.isPublic = true;

		if (!this.title.equals(title)) {
			this.title = title;
		}
	}

	private void validate(List<Object> values) {
		Validator.validateNotNull(values);
	}

	private void validateDate(Period period) {
		period.validate(period.getStartDate(), period.getEndDate());
	}

	public void delete() {
		this.isDeleted = true;
	}
}