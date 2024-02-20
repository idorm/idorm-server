package idorm.idormServer.community.comment.adapter.out.persistence;

import idorm.idormServer.community.comment.application.port.out.SaveCommentPort;
import idorm.idormServer.community.comment.domain.Comment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveCommentAdapter implements SaveCommentPort {

  private final CommentMapper commentMapper;
  private final CommentRepository commentRepository;

  @Override
  public void save(Comment comment) {
    final CommentJpaEntity commentJpaEntity = commentMapper.toEntity(comment);
    commentRepository.save(commentJpaEntity);
  }
}
