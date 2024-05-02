package idorm.idormServer.community.comment.application.port.out;

import idorm.idormServer.community.comment.entity.Comment;
import java.util.List;

public interface LoadCommentPort {

    Comment findById(Long commentId);

    Comment findOneByCommentIdAndPostId(Long commentId, Long postId);

    List<Comment> findAllByPostId(Long postId);

    List<Comment> findAllByMemberId(Long memberId);
}