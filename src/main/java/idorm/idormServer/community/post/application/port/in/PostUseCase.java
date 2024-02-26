package idorm.idormServer.community.post.application.port.in;

import java.util.List;

import org.springframework.data.domain.Page;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostListResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostSaveRequest;
import idorm.idormServer.community.post.application.port.in.dto.PostUpdateRequest;

public interface PostUseCase {

  void save(AuthResponse authResponse, PostSaveRequest request);

  void update(AuthResponse authResponse, Long postId, PostUpdateRequest request);

  void delete(AuthResponse authResponse, Long postId);

  Page<PostListResponse> findPostsByDormCategory(String dormCategory, int pageNum);

  List<PostListResponse> findTopPostsByDormCategory(String dormCategory);

  PostResponse findOneByPostId(AuthResponse authResponse, Long postId);

  List<PostListResponse> findPostsByMember(AuthResponse authResponse);

}
