package idorm.idormServer.fcm.dto;

import idorm.idormServer.fcm.domain.NotifyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmRequestDto {

    private String token;
    private Notification notification;
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Notification {
        private NotifyType notifyType;
        private Long contentId;
        private String tite;
        private String content;
    }
}
