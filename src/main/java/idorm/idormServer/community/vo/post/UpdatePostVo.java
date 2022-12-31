package idorm.idormServer.community.vo.post;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(title = "게시글 수정 VO")
public class UpdatePostVo {

    @NotBlank(message = "제목을 입력해 주세요.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "제목", example = "제에목")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요.")
    @ApiModelProperty(position = 3, required = true, dataType = "String", value = "내용", example = "내애용")
    private String content;

    @ApiModelProperty(position = 4, required = true, dataType = "Boolean", value = "익명 여부")
    private Boolean isAnonymous;

    @ApiModelProperty(position = 6, dataType = "List<MultipartFile>", value = "게시글 사진들")
    private List<MultipartFile> files = new ArrayList<>();
}
