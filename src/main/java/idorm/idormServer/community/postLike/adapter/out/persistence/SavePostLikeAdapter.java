package idorm.idormServer.community.postLike.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.postLike.application.port.out.SavePostLikePort;
import idorm.idormServer.community.postLike.entity.PostLike;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostLikeAdapter implements SavePostLikePort {

  private final PostLikeRepository postLikeMemberRepository;

  @Override
  public void save(PostLike postLike) {
    postLikeMemberRepository.save(postLike);
  }
}
