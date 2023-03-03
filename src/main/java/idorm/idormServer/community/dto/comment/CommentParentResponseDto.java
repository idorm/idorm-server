package idorm.idormServer.community.dto.comment;

import idorm.idormServer.community.domain.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "Comment 커스텀 부모 댓글 및 대댓글들 응답")
public class CommentParentResponseDto {

    @ApiModelProperty(position = 1, value= "댓글 식별자", required = true)
    private Long commentId;

    @ApiModelProperty(position = 3, value = "회원 식별자")
    private Long memberId;

    @ApiModelProperty(position = 2, value= "댓글 삭제 여부", required = true)
    private Boolean isDeleted;

    @ApiModelProperty(position = 3, value = "닉네임", allowableValues = "null(탈퇴한 회원), 익명1, 응철이",
            example = "익명1", required = true)
    private String nickname;

    @ApiModelProperty(position = 4, value = "프로필사진 주소", allowableValues = "null(프로필사진 없음/익명), url(익명 아님)",
            example = "null", required = true)
    private String profileUrl;

    @ApiModelProperty(position = 5, value = "댓글 내용", required = true)
    private String content;

    @ApiModelProperty(position = 6, value = "생성일자", required = true)
    private LocalDateTime createdAt;

    @ApiModelProperty(position = 7, value = "익명여부")
    private Boolean isAnonymous;

    @ApiModelProperty(position = 8, value = "대댓글들")
    private List<CommentDefaultResponseDto> subComments = new ArrayList<>();

    public CommentParentResponseDto(String anonymousNickname,
                                    Comment parentComment,
                                    List<CommentDefaultResponseDto> subComments) {

        this.commentId = parentComment.getId();
        this.isDeleted = parentComment.getIsDeleted();
        this.content = parentComment.getContent();
        this.createdAt = parentComment.getCreatedAt();
        this.isAnonymous = parentComment.getIsAnonymous();

        if (parentComment.getMember() != null)
            this.memberId = parentComment.getMember().getId();

        if (parentComment.getMember() == null) {
            this.nickname = null;
        } else if(parentComment.getIsAnonymous() == true) {
            this.nickname = anonymousNickname;
        } else if(parentComment.getIsAnonymous() == false) {
            this.nickname = parentComment.getMember().getNickname();
            if(parentComment.getMember().getProfilePhoto() != null) {
                this.profileUrl = parentComment.getMember().getProfilePhoto().getPhotoUrl();
            }
        }

        if (subComments != null) {
            for (CommentDefaultResponseDto subComment : subComments) {
                this.subComments.add(subComment);
            }
        }
    }
}
