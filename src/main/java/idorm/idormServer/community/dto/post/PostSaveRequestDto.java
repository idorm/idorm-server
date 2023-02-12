package idorm.idormServer.community.dto.post;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "Post 저장 요청")
@GroupSequence({PostSaveRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Size.class
})
public class PostSaveRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "커뮤니티 기숙사 분류", allowableValues = "DORM1, DORM2, DORM3",
            example = "DORM1")
    @NotBlank(message = "기숙사 분류를 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String dormCategory;

    @ApiModelProperty(position = 2, required = true, value = "제목", example = "제에목")
    @NotBlank(message = "제목을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    @Size(min = 1, max = 30, message = "게시글 제목은 1~30자로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String title;

    @ApiModelProperty(position = 3, required = true, value = "내용", example = "내애용")
    @NotBlank(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    @Size(min = 1, max = 300, message = "게시글 내용은 1~300자로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String content;

    @ApiModelProperty(position = 4, required = true, value = "익명 여부")
    @NotNull(message = "익명 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isAnonymous;

    @ApiModelProperty(position = 5, dataType = "List<MultipartFile>", value = "게시글 사진들")
    private List<MultipartFile> files = new ArrayList<>();
}
