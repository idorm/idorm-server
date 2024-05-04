package idorm.idormServer.community.post.adapter.out.persistence;

import static idorm.idormServer.community.post.entity.QPost.post;

import idorm.idormServer.community.post.adapter.out.exception.NotFoundPostException;
import idorm.idormServer.community.post.application.port.in.dto.querydsl.PostSearchResponse;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;

import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadPostAdapter implements LoadPostPort {

	private final PostRepository postRepository;

	@Override
	public Post findById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new NotFoundPostException());
	}

	@Override
	public Post findByIdAndMemberId(Long postId, Long memberId) {
		Post response = postRepository.findByIdAndMemberId(postId, memberId);
		if (post == null) {
			throw new NotFoundPostException();
		}
		return response;
	}

	@Override
	public Post findByIdForLikeUpdate(Long postId) {
		return postRepository.findByPostIdWithLock(postId)
			.orElseThrow(() -> new NotFoundPostException());
	}

	@Override
	public Page<PostSearchResponse> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable) {
		return postRepository.findPostsByDormCategoryAndIsDeletedFalse(dormCategory, pageable);
	}

	@Override
	public List<PostSearchResponse> findTopPostsByDormCateogry(DormCategory dormCategory) {
		List<PostSearchResponse> responses = postRepository.findTopPostsByDormCateogry(dormCategory);
		return responses != null ? responses : Collections.emptyList();
	}

	@Override
	public PostSearchResponse findTopPostByDormCategory(DormCategory dormCategory) {
		return postRepository.findTopPostByDormCategory(dormCategory);
	}

	@Override
	public List<PostSearchResponse> findPostsByMemberId(Long memberId) {
		List<PostSearchResponse> responses = postRepository.findPostsByMemberId(memberId);
		return responses != null ? responses : Collections.emptyList();
	}
}