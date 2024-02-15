package idorm.idormServer.calendar.adapter.out.persistence;

import static idorm.idormServer.calendar.domain.OfficialCalendar.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: 해당 클래스 전체 코드 리팩 대상

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendarJpaEntity {

	@Id
	@Column(name = "official_calendar_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 관리자 등록, 수정 가능
	private Boolean isDorm1Yn;
	private Boolean isDorm2Yn;
	private Boolean isDorm3Yn;

	private PeriodEmbeddedEntity period;

	// 크롤링 등록, 수정 가능
	@Column(nullable = false)
	private TitleEmbeddedEntity title;

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

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;
}