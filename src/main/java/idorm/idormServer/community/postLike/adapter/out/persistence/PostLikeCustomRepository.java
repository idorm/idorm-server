package idorm.idormServer.community.postLike.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import idorm.idormServer.community.postLike.entity.PostLike;

@Repository
public interface PostLikeCustomRepository {

	List<PostLike> findByMemberId(Long memberId);
}
