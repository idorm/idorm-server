package idorm.idormServer.community.dto.post;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "Post 수정 요청")
@GroupSequence({PostUpdateRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class
})
public class PostUpdateRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "제목", example = "제에목")
    @NotBlank(message = "제목을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String title;

    @ApiModelProperty(position = 2, required = true, value = "내용", example = "내애용")
    @NotBlank(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String content;

    @ApiModelProperty(position = 3, required = true, value = "익명 여부")
    @NotNull(message = "익명 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isAnonymous;

    @ApiModelProperty(position = 4, value = "게시글 사진들")
    private List<MultipartFile> files = new ArrayList<>();
}
