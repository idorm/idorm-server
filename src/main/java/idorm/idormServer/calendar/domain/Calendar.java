package idorm.idormServer.calendar.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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
}
