package idorm.idormServer.community.dto;

import idorm.idormServer.common.ValidationSequence;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({CommentRequest.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Size.class,
        ValidationSequence.Positive.class
})
@Schema(title = "Comment 기본 요청")
public class CommentRequest {

    @Schema(required = true, description = "내용", example = "댓글내용")
    @NotBlank(message = "댓글 내용을 입력 해주세요.", groups = ValidationSequence.NotBlank.class)
    @Size(min = 1, max = 50, message = "댓글 내용은 1~50자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String content;

    @Schema(required = true, description = "익명 여부", example = "true")
    @NotNull(message = "익명 여부를 입력 해주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isAnonymous;

    @Schema(description = "대댓글일 시 부모 댓글 식별자", allowableValues = "null(댓글), 1(대댓글일 때 댓글 식별자)",
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
