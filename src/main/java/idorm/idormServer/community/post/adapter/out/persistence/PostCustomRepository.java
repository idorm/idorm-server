package idorm.idormServer.community.post.adapter.out.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;

@Repository
public interface PostCustomRepository {

	Post findByIdAndMemberId(Long postId, Long memberId);

	Page<Post> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable);

	List<Post> findTopPostsByDormCateogry(DormCategory dormCategory);

	Post findTopPostByDormCategory(DormCategory dormCategory);

	List<Post> findPostsByMemberId(Long memberId);
}
