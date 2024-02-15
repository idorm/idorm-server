package idorm.idormServer.community.application.port.in.dto;

import idorm.idormServer.community.domain.PostPhoto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(title = "PostPhotoJpaEntity 기본 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhotoResponse {

    @Schema(required = true, description = "게시글 사진 식별자", example = "1")
    private Long photoId;

    @Schema(required = true, description = "사진 URL")
    private String photoUrl;

    public PostPhotoResponse(PostPhoto postPhoto) {
        this.photoId = postPhoto.getId();
        this.photoUrl = postPhoto.getPhotoUrl();
    }
}
