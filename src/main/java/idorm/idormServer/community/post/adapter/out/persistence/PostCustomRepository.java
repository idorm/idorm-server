package idorm.idormServer.community.post.adapter.out.persistence;

import idorm.idormServer.community.post.application.port.in.dto.PostListData;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCustomRepository {

	Post findByIdAndMemberId(Long postId, Long memberId);

	Page<PostListData> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable);

	List<PostListData> findTopPostsByDormCateogry(DormCategory dormCategory);

	PostListData findTopPostByDormCategory(DormCategory dormCategory);

	List<PostListData> findPostsByMemberId(Long memberId);
}