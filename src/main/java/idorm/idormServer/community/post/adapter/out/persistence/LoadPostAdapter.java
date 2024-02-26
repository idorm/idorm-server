package idorm.idormServer.community.post.adapter.out.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import idorm.idormServer.community.post.adapter.out.exception.NotFoundPostException;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadPostAdapter implements LoadPostPort {

	private final PostRepository postRepository;

	@Override
	public Post findById(Long postId) {
		Post response = postRepository.findById(postId)
			.orElseThrow(() -> new NotFoundPostException());
		return response;
	}

	@Override
	public Post findByIdAndIsDeletedIsFalse(Long postId) {
		Post response = postRepository.findByIdAndIsDeletedIsFalse(postId);
		return response;
	}

	@Override
	public Page<Post> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable) {
		Page<Post> responses = postRepository.findAllByDormCategoryAndIsDeletedIsFalseOrderByCreatedAtDesc(
			dormCategory, pageable);
		return responses.isEmpty() ? null : responses;
	}

	@Override
	public List<Post> findTopPostsByDormCateogry(DormCategory dormCategory) {
		List<Post> responses = postRepository.findTopPostsByDormCategory(dormCategory);
		return responses.isEmpty() ? null : responses;
	}

	@Override
	public List<Post> findPostsByMemberId(Long memberId) {
		List<Post> responses = postRepository.findAllByMemberIdAndIsDeletedIsFalseOrderByUpdatedAtDesc(
			memberId);
		return responses.isEmpty() ? null : responses;
	}

	@Override
	public List<Post> findLikePostsByMemberId(Long memberId) {
		List<Post> responses = postRepository.findLikePostsByMemberId(memberId);
		return responses.isEmpty() ? null : responses;
	}
}
