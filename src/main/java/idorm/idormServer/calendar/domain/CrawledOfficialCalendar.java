package idorm.idormServer.calendar.domain;

import idorm.idormServer.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrawledOfficialCalendar extends BaseEntity {

    @Id
    @Column(name = "crawled_official_calendar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String websiteUrl;
    private String inuPostId;

    @Builder
    public CrawledOfficialCalendar(String title, String websiteUrl, String inuPostId) {
        this.title = title;
        this.websiteUrl = websiteUrl;
        this.inuPostId = inuPostId;

        this.setIsDeleted(false);
    }

    public void delete() {
        this.setIsDeleted(true);
    }
}
