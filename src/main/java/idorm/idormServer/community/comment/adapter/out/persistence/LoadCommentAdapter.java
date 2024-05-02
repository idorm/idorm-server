package idorm.idormServer.community.comment.adapter.out.persistence;

import idorm.idormServer.community.comment.adapter.out.exception.NotFoundCommentException;
import idorm.idormServer.community.comment.application.port.out.LoadCommentPort;
import idorm.idormServer.community.comment.entity.Comment;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadCommentAdapter implements LoadCommentPort {

    private final CommentRepository commentRepository;

    @Override
    public Comment findById(Long commentId) {
        Comment response = commentRepository.findByIdAndIsDeletedIsFalse(commentId)
            .orElseThrow(NotFoundCommentException::new);
        return response;
    }

    @Override
    public Comment findOneByCommentIdAndPostId(Long commentId, Long postId) {
        Comment response = commentRepository.findOneByCommentIdAndPostId(commentId, postId);
        if (response == null) {
            throw new NotFoundCommentException();
        }
        return response;
    }

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        List<Comment> responses = commentRepository.findAllByPostId(postId);
        return responses != null ? responses : Collections.emptyList();
    }

    @Override
    public List<Comment> findAllByMemberId(Long memberId) {
        List<Comment> responses = commentRepository.findAllByMemberId(memberId);
        return responses != null ? responses : Collections.emptyList();
    }
}