package idorm.idormServer.community.post.application.port.out;

import idorm.idormServer.community.post.application.port.in.dto.querydsl.PostSearchResponse;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoadPostPort {

	Post findById(Long postId);

	Post findByIdAndMemberId(Long postId, Long memberId);

	Post findByIdForLikeUpdate(Long postId);

	Page<PostSearchResponse> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable);

	List<PostSearchResponse> findTopPostsByDormCateogry(DormCategory dormCategory);

	PostSearchResponse findTopPostByDormCategory(DormCategory dormCategory);

	List<PostSearchResponse> findPostsByMemberId(Long memberId);
}