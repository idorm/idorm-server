package idorm.idormServer.community.dto.post;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.domain.Photo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "Post 응답")
public class PostDefaultResponseDto {

    @ApiModelProperty(position = 1, value="게시글 식별자")
    private Long postId;

    @ApiModelProperty(position = 2, value="작성자 식별자")
    private Long memberId;

    @ApiModelProperty(position = 3, value = "커뮤니티 카테고리")
    private String dormNum;

    @ApiModelProperty(position = 4, value = "게시글 제목")
    private String title;

    @ApiModelProperty(position = 5, value = "게시글 내용")
    private String content;

    @ApiModelProperty(position = 6, value = "게시글 익명 여부")
    private Boolean isAnonymous;

    @ApiModelProperty(position = 7, value = "게시글 공개 여부")
    private Boolean isVisible;

    @ApiModelProperty(position = 8, value = "업로드 사진 url")
    private List<String> photoUrls = new ArrayList<>();

//    @ApiModelProperty(position = 9, value = "공감한 멤버 식별자")
//    private List<Long> postLikedMemberIds = new ArrayList<>();
//
//    @ApiModelProperty(position = 10, value = "댓글 식별자")
//    private List<Long> commentIds = new ArrayList<>();

    public PostDefaultResponseDto(Post post) {
        this.postId = post.getId();
        this.memberId = post.getMember().getId();
        this.dormNum = post.getDormNum();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isAnonymous = post.getIsAnonymous();
        this.isVisible = post.getIsVisible();

        if(post.getPhotos() != null) {
            for(Photo photo : post.getPhotos()) {
                this.photoUrls.add(photo.getUrl());
            }
        }

//        if(post.getLikes() != null) {
//            for(Member member : post.getLikes()) {
//                this.postLikedMemberIds.add(member.getId());
//            }
//        }
//
//        if(post.getComments() != null) {
//            for(Comment comment : post.getComments()) {
//                this.commentIds.add(comment.getId());
//            }
//        }
    }
}