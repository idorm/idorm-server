package idorm.idormServer.community.application.port.in;

import idorm.idormServer.community.application.port.in.dto.PostSaveRequest;
import idorm.idormServer.community.application.port.in.dto.PostUpdateRequest;
import idorm.idormServer.member.domain.Member;

public interface PostUseCase {

	void findPostsFilteredByCategory(Member member, String dormCategoryRequest, int pageNum);

	void findTopPostsFilteredByCategory(Member member, String dormCategoryRequest);

	void findOnePost(Member member, Long postId);

	void save(Member member, PostSaveRequest request);

	void update(Member member, Long postId, PostUpdateRequest request);

	void findPostsByMember(Member member);

	void delete(Member member, Long postId);

}
