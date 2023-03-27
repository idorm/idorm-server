//package idorm.idormServer.calendar.dto;
//
//import idorm.idormServer.calendar.domain.Calendar;
//import lombok.Getter;
//
//import java.time.LocalDateTime;
//
//@Getter
//public class CalendarRequest {
//    private String url;
//    private String title;
//    private String content;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
//    private String imageUrl;
//    private String notificationYn;
//    private String officialYn;
//    private String together;
//
//    public Calendar toEntity(Long id, Long memberId) {
//
//        return new Calendar(
//                id,
//                memberId,
//                url,
//                title,
//                content,
//                startTime,
//                endTime,
//                imageUrl,
//                notificationYn,
//                officialYn,
//                together
//        );
//    }
//}
