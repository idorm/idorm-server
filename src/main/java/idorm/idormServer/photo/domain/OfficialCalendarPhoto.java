package idorm.idormServer.photo.domain;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import idorm.idormServer.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendarPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "official_calendar_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "official_calendar_id")
    private OfficialCalendar officialCalendar;

    private String photoUrl;
    private String inuPostId;

    @Builder
    public OfficialCalendarPhoto(String photoUrl,
                                 String inuPostId) {
        this.officialCalendar = null;
        this.photoUrl = photoUrl;
        this.inuPostId = inuPostId;
        this.setIsDeleted(false);
    }

    public void updateOfficialCalendar(OfficialCalendar officialCalendar) {
        this.officialCalendar = officialCalendar;
    }

    public void removePhotoUrl() {
        this.photoUrl = null;
    }

    public void delete() {
        this.setIsDeleted(true);
    }
}
