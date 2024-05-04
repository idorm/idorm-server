package idorm.idormServer.community.post.adapter.out.persistence;

import idorm.idormServer.community.post.application.port.in.dto.querydsl.PostSearchResponse;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepositoryCustom {

	Post findByIdAndMemberId(Long postId, Long memberId);

	Page<PostSearchResponse> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable);

	List<PostSearchResponse> findTopPostsByDormCateogry(DormCategory dormCategory);

	PostSearchResponse findTopPostByDormCategory(DormCategory dormCategory);

	List<PostSearchResponse> findPostsByMemberId(Long memberId);
}