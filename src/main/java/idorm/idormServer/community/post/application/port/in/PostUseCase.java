package idorm.idormServer.community.post.application.port.in;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostOneSummaryResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostSaveRequest;
import idorm.idormServer.community.post.application.port.in.dto.PostUpdateRequest;

import java.util.List;

import org.springframework.data.domain.Page;

public interface PostUseCase {

	PostResponse save(AuthResponse authResponse, PostSaveRequest request);

	void update(AuthResponse authResponse, Long postId, PostUpdateRequest request);

	void delete(AuthResponse authResponse, Long postId);

	Page<PostOneSummaryResponse> findPostsByDormCategory(String dormCategory, int pageNum);

	List<PostOneSummaryResponse> findTopPostsByDormCategory(String dormCategory);

	PostResponse findOneByPostId(AuthResponse authResponse, Long postId);

	List<PostOneSummaryResponse> findPostsByMember(AuthResponse authResponse);
}