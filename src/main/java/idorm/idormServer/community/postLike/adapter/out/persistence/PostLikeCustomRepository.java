package idorm.idormServer.community.postLike.adapter.out.persistence;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeCustomRepository {

    List<Long> findPostIdsByMemberId(Long memberId);
}