package idorm.idormServer.community.dto.post;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.dto.comment.CommentDefaultResponseDto;
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
@ApiModel(value = "Post 단건 응답")
public class PostOneResponseDto {

    @ApiModelProperty(position = 1, value="게시글 식별자", example = "1")
    private Long postId;

    @ApiModelProperty(position = 2, value = "게시글 제목", example = "제에목")
    private String title;

    @ApiModelProperty(position = 3, value = "게시글 내용", example = "내애용")
    private String content;

    @ApiModelProperty(position = 4, value = "닉네임", example = "null(탈퇴), anonymous(익명), 응철이")
    private String nickname;

    @ApiModelProperty(position = 5, value = "프로필사진 주소", example = "null(사진이 없거나, 익명), url")
    private String profileUrl;

    @ApiModelProperty(position = 6, value = "공감 수")
    private int likesCount;

    @ApiModelProperty(position = 7, value = "댓글 수")
    private int commentsCount;

    @ApiModelProperty(position = 8, value = "이미지 수")
    private int imagesCount;

    @ApiModelProperty(position = 9, value = "생성일자")
    private LocalDateTime createdAt;

    @ApiModelProperty(position = 10, value = "수정일자")
    private LocalDateTime updatedAt;

    @ApiModelProperty(position = 11, value = "업로드한 사진 주소 목록")
    private List<String> photoUrls = new ArrayList<>();

    @ApiModelProperty(position = 12, value = "댓글/대댓글 목록")
    private List<CommentDefaultResponseDto> comments = new ArrayList<>();

    public PostOneResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();

        this.likesCount = post.getLikesCount();
        this.commentsCount = post.getCommentsCount();
        this.imagesCount = post.getImagesCount();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

        if(post.getMember() == null) { // 회원 탈퇴의 경우
            this.nickname = null;
        } else if(post.getIsAnonymous() == false) { // 익명이 아닌 경우
            this.nickname = post.getMember().getNickname();
            if(post.getMember().getPhoto() != null) {
                this.profileUrl = post.getMember().getPhoto().getUrl();
            }
        } else if(post.getIsAnonymous() == true) { // 익명일 경우
            this.nickname = "anonymous";
        }

        if(post.getPhotos() != null) {
            for(Photo photo : post.getPhotos()) {
                this.photoUrls.add(photo.getUrl());
            }
        }

        if(post.getComments() != null) {
            for(Comment comment : post.getComments()) {

                CommentDefaultResponseDto commentDto = new CommentDefaultResponseDto(comment);
                this.comments.add(commentDto);
            }
        }
    }
}