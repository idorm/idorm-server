package idorm.idormServer.fcm.application.port.in.dto;

import idorm.idormServer.fcm.domain.NotifyType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmRequest {

    @Setter
    private String token;
    private Notification notification;
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Notification {
        private NotifyType notifyType;
        private Long contentId;
        private String title;
        private String content;
    }
}
