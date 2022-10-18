package idorm.idormServer.community.dto.post;

import idorm.idormServer.community.domain.Post;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@ApiModel(value = "Post 저장 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PostDefaultRequestDto {

    @ApiModelProperty(position = 1, required = true, dataType = "Long", value = "멤버 식별자", example = "2")
    private Long memberId;

    @NotBlank(message = "기숙사 분류를 입력해 주세요.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "커뮤니티 기숙사 분류", example = "DORM1")
    private String dormNum;

    @NotBlank(message = "제목을 입력해 주세요.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "제목", example = "제목제목")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요.")
    @ApiModelProperty(position = 3, required = true, dataType = "String", value = "내용", example = "내용내용")
    private String content;

    @ApiModelProperty(position = 4, required = true, dataType = "Boolean", value = "익명 여부", example = "true")
    private Boolean isAnonymous;

    public PostDefaultRequestDto(Post post) {
        this.dormNum = post.getDormNum();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isAnonymous = post.getIsAnonymous();
    }
}
