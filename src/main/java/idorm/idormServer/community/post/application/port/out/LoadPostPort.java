package idorm.idormServer.community.post.application.port.out;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;

public interface LoadPostPort {

	Post findById(Long postId);

	Post findByIdAndIsDeletedIsFalse(Long postId);

	Page<Post> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable);

	List<Post> findLikePostsByMemberId(Long memberId);

	List<Post> findTopPostsByDormCateogry(DormCategory dormCategory);

	List<Post> findPostsByMemberId(Long memberId);
}
