package idorm.idormServer.fcm.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class FcmMessage {
    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String alertType;
        private String title;
        private String body;
        private String image;
    }
}
