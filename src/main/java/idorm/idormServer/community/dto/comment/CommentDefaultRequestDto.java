package idorm.idormServer.community.dto.comment;

import idorm.idormServer.common.ValidationSequence;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({CommentDefaultRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Size.class,
        ValidationSequence.Positive.class
})
@ApiModel(value = "Comment 기본 요청")
public class CommentDefaultRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "내용", example = "댓글내용")
    @NotBlank(message = "댓글 내용을 입력 해주세요.", groups = ValidationSequence.NotBlank.class)
    @Size(min = 1, max = 50, message = "댓글 내용은 1~50자로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String content;

    @ApiModelProperty(position = 2, required = true, value = "익명 여부", example = "true")
    @NotNull(message = "익명 여부를 입력 해주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isAnonymous;

    @ApiModelProperty(position = 3, value = "대댓글일 시 부모 댓글 식별자", allowableValues = "null(댓글), 1(대댓글일 때 댓글 식별자)",
            example = "null")
    @Positive(message = "부모 댓글 식별자는 양수만 가능합니다.", groups = ValidationSequence.Positive.class)
    private Long parentCommentId;

    public Comment toEntity(Member member, Post post) {
        return Comment.builder()
                .content(this.content)
                .isAnonymous(this.isAnonymous)
                .post(post)
                .member(member)
                .build();
    }
}
