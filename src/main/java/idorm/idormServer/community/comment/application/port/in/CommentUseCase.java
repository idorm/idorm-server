package idorm.idormServer.community.comment.application.port.in;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.community.comment.application.port.in.dto.CommentRequest;
import idorm.idormServer.community.comment.application.port.in.dto.CommentResponse;
import java.util.List;

public interface CommentUseCase {

  void save(AuthResponse authResponse, Long postId, CommentRequest request);

  void delete(AuthResponse authResponse, Long postId, Long commentId);

  List<CommentResponse> findCommentsByMember(AuthResponse authResponse);
}
