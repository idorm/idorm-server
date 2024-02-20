package idorm.idormServer.community.postLike.adapter.out.persistence;

import idorm.idormServer.community.postLike.application.port.out.SavePostLikePort;
import idorm.idormServer.community.postLike.domain.PostLike;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostLikeAdapter implements SavePostLikePort {

  private final PostLikeMapper postLikeMapper;
  private final PostLikeMemberRepository postLikeMemberRepository;

  @Override
  public void save(PostLike postLike) {
    PostLikeJpaEntity postLikeJpaEntity = postLikeMapper.toEntity(postLike);
    postLikeMemberRepository.save(postLikeJpaEntity);
  }
}
