package idorm.idormServer.community.application.port.in.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "PostJpaEntity 저장 요청")
public class PostSaveRequest {

    @Schema(required = true, description = "커뮤니티 기숙사 분류", allowableValues = "DORM1, DORM2, DORM3",
            example = "DORM1")
    private String dormCategory;

    @Schema(required = true, description = "제목", example = "제에목")
    private String title;

    @Schema(required = true, description = "내용", example = "내애용")
    private String content;

    @Schema(required = true, description = "익명 여부")
    private Boolean isAnonymous;

    @Schema(description = "게시글 사진들")
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
