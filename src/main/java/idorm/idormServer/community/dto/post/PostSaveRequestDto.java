package idorm.idormServer.community.dto.post;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "Post 저장 요청")
public class PostSaveRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "커뮤니티 기숙사 분류", allowableValues = "DORM1, DORM2, DORM3",
            example = "DORM1")
    private String dormCategory;

    @ApiModelProperty(position = 2, required = true, value = "제목", example = "제에목")
    private String title;

    @ApiModelProperty(position = 3, required = true, value = "내용", example = "내애용")
    private String content;

    @ApiModelProperty(position = 4, required = true, value = "익명 여부")
    private Boolean isAnonymous;

    @ApiModelProperty(position = 5, dataType = "List<MultipartFile>", value = "게시글 사진들")
    private List<MultipartFile> files = new ArrayList<>();

    public Post toEntity(Member member) {
        return Post.builder()
                .dormCategory(DormCategory.validateType(this.dormCategory))
                .title(this.title)
                .content(content)
                .isAnonymous(this.isAnonymous)
                .member(member)
                .build();
    }
}
