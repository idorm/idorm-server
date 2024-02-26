package idorm.idormServer.community.comment.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.comment.application.port.out.DeleteCommentPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteCommentAdapter implements DeleteCommentPort {

  private final CommentRepository commentRepository;

  @Override
  public void deleteById(Long commentId) {
    commentRepository.deleteById(commentId);
  }
}
