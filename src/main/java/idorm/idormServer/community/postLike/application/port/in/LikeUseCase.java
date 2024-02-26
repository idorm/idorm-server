package idorm.idormServer.community.postLike.application.port.in;

import java.util.List;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostListResponse;

public interface LikeUseCase {

  void save(AuthResponse authResponse, Long postId);

  void delete(AuthResponse authResponse, Long postId);

  List<PostListResponse> findLikedPostsByMember(AuthResponse authResponse);
}
