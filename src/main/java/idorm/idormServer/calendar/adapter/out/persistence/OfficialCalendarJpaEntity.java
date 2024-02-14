package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.common.domain.BaseTimeEntity;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: 해당 클래스 전체 코드 리팩 대상

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendarJpaEntity extends BaseTimeEntity {

    private static final String CRAWLING_FAIL_VALUE = "fail";
    private static final LocalDate CRAWLING_FAIL_DATE = LocalDate.of(2020, 1, 1);
    private static final String BLANK_TITLE_MESSAGE = "크롤링 실패 : 게시글 제목";
    private static final String BLANK_INU_POST_ID_MESSAGE = "크롤링 실패 : 게시글 ID";

    private static final int MAX_INU_POST_ID_LENGTH = 10;

    @Id
    @Column(name = "official_calendar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 관리자 등록, 수정 가능
    private Boolean isDorm1Yn;
    private Boolean isDorm2Yn;
    private Boolean isDorm3Yn;

    private PeriodEmbeddedEntity periodEmbeddedEntity;

    // 크롤링 등록, 수정 가능
    @NotBlank
    @Column(nullable = false)
    private TitleEmbeddedEntity titleEmbeddedEntity;

    @NotNull
    @Column(nullable = false)
    private Boolean isPublic;

    // 크롤링 등록, 수정 불가
    @NotBlank
    @Column(nullable = false, length = MAX_INU_POST_ID_LENGTH)
    private String inuPostId;

    @NotBlank
    @Column(nullable = false)
    private String inuPostUrl;

    @NotNull
    @Column(nullable = false)
    private LocalDate inuPostCreatedAt;

    @NotNull
    @Column(nullable = false)
    private Boolean isDeleted;
}