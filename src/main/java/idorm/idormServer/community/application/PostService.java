package idorm.idormServer.community.application;

import org.springframework.stereotype.Service;

import idorm.idormServer.community.application.port.in.PostUseCase;
import idorm.idormServer.community.application.port.in.dto.PostSaveRequest;
import idorm.idormServer.community.application.port.in.dto.PostUpdateRequest;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService implements PostUseCase {

	@Override
	public void findPostsFilteredByCategory(Member member, String dormCategoryRequest, int pageNum) {

	}

	@Override
	public void findTopPostsFilteredByCategory(Member member, String dormCategoryRequest) {

	}

	@Override
	public void findOnePost(Member member, Long postId) {

	}

	@Override
	public void save(Member member, PostSaveRequest request) {

	}

	@Override
	public void update(Member member, Long postId, PostUpdateRequest request) {

	}

	@Override
	public void findPostsByMember(Member member) {

	}

	@Override
	public void delete(Member member, Long postId) {

	}
}