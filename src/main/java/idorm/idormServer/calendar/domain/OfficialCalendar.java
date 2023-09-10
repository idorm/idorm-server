package idorm.idormServer.calendar.domain;

import idorm.idormServer.calendar.dto.OfficialCalendarUpdateRequest;
import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.photo.domain.OfficialCalendarPhoto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendar extends BaseEntity {

    @Id
    @Column(name = "official_calendar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inuPostId;

    private Boolean isDorm1Yn;
    private Boolean isDorm2Yn;
    private Boolean isDorm3Yn;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String title;
    private String content;
    private String location;
    private String websiteUrl;

    @OneToMany(mappedBy = "officialCalendar")
    private List<OfficialCalendarPhoto> officialCalendarPhotos = new ArrayList<>();

    @Builder
    public OfficialCalendar(String inuPostId,
                            Boolean isDorm1Yn,
                            Boolean isDorm2Yn,
                            Boolean isDorm3Yn,
                            LocalDate startDate,
                            LocalDate endDate,
                            LocalTime startTime,
                            LocalTime endTime,
                            String title,
                            String content,
                            String location,
                            String url) {

        this.inuPostId = inuPostId;
        this.isDorm1Yn = isDorm1Yn;
        this.isDorm2Yn = isDorm2Yn;
        this.isDorm3Yn = isDorm3Yn;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.content = content;
        this.location = location;
        this.websiteUrl = url;

        // TODO: inuPostId 를 가진 OfficialCalendarPhoto 에 OfficialCalendar 매핑

        this.setIsDeleted(false);
    }

    public List<OfficialCalendarPhoto> getOfficialCalendarPhotos() {

        List<OfficialCalendarPhoto> officialCalendarPhotoList = new ArrayList<>(this.officialCalendarPhotos);
        officialCalendarPhotoList.removeIf(c -> c.getIsDeleted().equals(true));
        return officialCalendarPhotoList;
    }

    public void update(OfficialCalendarUpdateRequest request) {
        this.inuPostId = request.getInuPostId();
        this.isDorm1Yn = request.getIsDorm1Yn();
        this.isDorm3Yn = request.getIsDorm3Yn();
        this.startDate = request.getStartDate();
        this.isDorm2Yn = request.getIsDorm2Yn();
        this.endDate = request.getEndDate();
        this.startTime =request.getStartTime();
        this.endTime = request.getEndTime();
        this.title = request.getTitle();
        this.content = request.getContent();
        this.location = request.getLocation();
        this.websiteUrl = request.getUrl();
    }

    public void updateIsDorm1Yn(Boolean isDorm1Yn) {
        this.isDorm1Yn = isDorm1Yn;
    }

    public void updateIsDorm2Yn(Boolean isDorm2Yn) {
        this.isDorm2Yn = isDorm2Yn;
    }

    public void updateIsDorm3Yn(Boolean isDorm3Yn) {
        this.isDorm3Yn = isDorm3Yn;
    }

    public void updateStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void updateEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void updateStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void updateEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateLocation(String location) {
        this.location = location;
    }

    public void updateUrl(String url) {
        this.websiteUrl = url;
    }

    public void delete() {
        this.setIsDeleted(true);
    }
}
