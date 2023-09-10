package idorm.idormServer.community.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(title = "Post 수정 요청")
public class PostUpdateRequest {

    @Schema(required = true, description = "제목", example = "제에목")
    private String title;

    @Schema(required = true, description = "내용", example = "내애용")
    private String content;

    @Schema(required = true, description = "익명 여부")
    private Boolean isAnonymous;

    @Schema(description = "삭제할 게시글 사진 식별자들")
    private List<Long> deletePostPhotoIds = new ArrayList<>();

    @Schema(description = "게시글 사진들")
    private List<MultipartFile> files = new ArrayList<>();
}
