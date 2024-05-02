package idorm.idormServer.community.comment.adapter.out.persistence;

import idorm.idormServer.community.comment.application.port.out.SaveCommentPort;
import idorm.idormServer.community.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveCommentAdapter implements SaveCommentPort {

    private final CommentRepository commentRepository;

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}