package idorm.idormServer.community.dto.post;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.photo.domain.Photo;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "Post 응답")
public class PostDefaultResponseDto {

    @ApiModelProperty(position = 1, value="게시글 식별자")
    private Long postId;

    @ApiModelProperty(position = 4, value = "게시글 제목")
    private String title;

    @ApiModelProperty(position = 5, value = "게시글 내용")
    private String content;

    @ApiModelProperty(position = 6, value = "닉네임", example = "null(탈퇴), anonymous(익명), 응철이")
    private String nickname;

    @ApiModelProperty(position = 8, value = "공감 수")
    private int likesCount;

    @ApiModelProperty(position = 9, value = "댓글 수")
    private int commentsCount;

    @ApiModelProperty(position = 10, value = "이미지 수")
    private int imagesCount;

    @ApiModelProperty(position = 10, value = "생성일자")
    private LocalDateTime createdAt;

    public PostDefaultResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();

        this.likesCount = post.getLikesCount();
        this.commentsCount = post.getCommentsCount();
        this.imagesCount = post.getImagesCount();
        this.createdAt = post.getCreatedAt();

        if(post.getMember() == null) { // 회원 탈퇴의 경우
            this.nickname = null;
        } else if(post.getIsAnonymous() == false) { // 익명이 아닌 경우
            this.nickname = post.getMember().getNickname();
        } else if(post.getIsAnonymous() == true) { // 익명일 경우
            this.nickname = "anonymous";
        }
    }

    public PostDefaultResponseDto(Post post, String myNickname) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();

        this.likesCount = post.getLikesCount();
        this.commentsCount = post.getCommentsCount();
        this.imagesCount = post.getImagesCount();
        this.createdAt = post.getCreatedAt();
        this.nickname = myNickname;
    }

}
