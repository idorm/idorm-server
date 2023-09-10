package idorm.idormServer.fcm.dto;

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
