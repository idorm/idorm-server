package idorm.idormServer.community.dto.post;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "Post 수정 요청")
public class PostUpdateRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "제목", example = "제에목")
    private String title;

    @ApiModelProperty(position = 2, required = true, value = "내용", example = "내애용")
    private String content;

    @ApiModelProperty(position = 3, required = true, value = "익명 여부")
    private Boolean isAnonymous;

    @ApiModelProperty(position = 4, value = "삭제할 게시글 사진 식별자들")
    private List<Long> deletePostPhotoIds = new ArrayList<>();

    @ApiModelProperty(position = 5, value = "게시글 사진들")
    private List<MultipartFile> files = new ArrayList<>();
}
