package idorm.idormServer.calendar.dto;

import idorm.idormServer.calendar.domain.Calendar;

import java.time.LocalDateTime;

public class CalendarRequest {
    private Long memberId;
    private String url;
    private String title;
    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String imageUrl;
    private String notificationYn;
    private String officialYn;
    private String together;

    public Calendar toEntity(Long id) {

        return new Calendar(
                id,
                memberId,
                url,
                title,
                content,
                startTime,
                endTime,
                imageUrl,
                notificationYn,
                officialYn,
                together
        );
    }
}
