//package idorm.idormServer.community.dto.comment;
//
//import idorm.idormServer.community.domain.Comment;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@ApiModel(value = "Comment 게시글 내 모든 댓글 대댓글 커스텀 응답")
//public class CommentCustomResponseDto {
//
//    @ApiModelProperty(position = 1, value="댓글 식별자")
//    private Long commentId;
//
//    @ApiModelProperty(position = 3, value="게시글 식별자")
//    private Long postId;
//
//    @ApiModelProperty(position = 4, value="작성자 식별자")
//    private Long memberId;
//
//    @ApiModelProperty(position = 5, value = "댓글 내용")
//    private String content;
//
//    @ApiModelProperty(position = 6, value = "댓글 익명 여부")
//    private Boolean isAnonymous;
//
//    @ApiModelProperty(position = 7, value = "댓글 삭제 여부")
//    private Boolean isDeleted;
//
//    @ApiModelProperty(position = 8, value = "생성일자")
//    private LocalDateTime createdAt;
//
//    @ApiModelProperty(position = 9, value = "수정일자")
//    private LocalDateTime updatedAt;
//
//    @ApiModelProperty(position = 10, value = "대댓글들")
//    private List<CommentDefaultResponseDto> subComments = new ArrayList<>();
//
//
//    public CommentCustomResponseDto(Comment parentComment, List<CommentDefaultResponseDto> subComments) {
//        this.commentId = parentComment.getId();
//        this.postId = parentComment.getPost().getId();
//        this.memberId = parentComment.getMember().getId();
//        this.content = parentComment.getContent();
//        this.isAnonymous = parentComment.getIsAnonymous();
//        this.isDeleted = parentComment.getIsDeleted();
//        this.createdAt = parentComment.getCreatedAt();
//        this.updatedAt = parentComment.getUpdatedAt();
//
//        for (CommentDefaultResponseDto subComment : subComments) {
//            this.subComments.add(subComment);
//        }
//    }
//}