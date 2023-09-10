package idorm.idormServer.calendar.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import idorm.idormServer.photo.domain.OfficialCalendarPhoto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(title = "공식 일정 첨부 사진 기본 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendarPhotoResponse {

    @Schema(required = true, name = "photoId", description = "공식 일정 사진 식별자", example = "1")
    @JsonProperty("photoId")
    private Long photoId;

    @Schema(name = "photoUrl", required = true, description = "사진 URL")
    @JsonProperty("photoUrl")
    private String photoUrl;

    public OfficialCalendarPhotoResponse(OfficialCalendarPhoto calendarPhoto) {
        this.photoId = calendarPhoto.getId();
        this.photoUrl = calendarPhoto.getPhotoUrl();
    }
}
