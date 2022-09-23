package idorm.idormServer.calendar.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Calendar {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long calendarId;
    private Long memberId;
    private String url;
    private String title;
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String imageUrl;
    private String notificationYn;
    private String officialYn;
    private String together;

    public Calendar(Long calendarId, Long memberId, String url, String title,
                    String content, LocalDateTime startTime, LocalDateTime endTime,
                    String imageUrl, String notificationYn, String officialYn, String together) {
        this.calendarId = calendarId;
        this.memberId = memberId;
        this.url = url;
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.imageUrl = imageUrl;
        this.notificationYn = notificationYn;
        this.officialYn = officialYn;
        this.together = together;
    }
}
