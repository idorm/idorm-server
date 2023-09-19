package idorm.idormServer.calendar.domain;

import idorm.idormServer.calendar.dto.OfficialCalendarUpdateRequest;
import idorm.idormServer.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendar extends BaseEntity {

    @Id
    @Column(name = "official_calendar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 관리자 등록
    private Boolean isDorm1Yn;
    private Boolean isDorm2Yn;
    private Boolean isDorm3Yn;
    private LocalDate startDate;
    private LocalDate endDate;

    // 수정 가능
    private String title;

    // 수정 불가
    private String inuPostId;
    private String postUrl;
    private LocalDate inuPostCreatedAt;
    private Boolean isPublic; // default: false

    @Builder
    public OfficialCalendar(String inuPostId,
                            String title,
                            String postUrl,
                            LocalDate inuPostCreatedAt
    ) {

        this.inuPostId = inuPostId;
        this.title = title;
        this.postUrl = postUrl;
        this.inuPostCreatedAt = inuPostCreatedAt;

        this.isPublic = false;
        this.setIsDeleted(false);
    }

    public void update(OfficialCalendarUpdateRequest request) {
        this.isDorm1Yn = request.getIsDorm1Yn();
        this.isDorm2Yn = request.getIsDorm2Yn();
        this.isDorm3Yn = request.getIsDorm3Yn();

        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();

        this.title = request.getTitle();

        this.isPublic = true;
    }

    public void delete() {
        this.setIsDeleted(true);
    }
}
