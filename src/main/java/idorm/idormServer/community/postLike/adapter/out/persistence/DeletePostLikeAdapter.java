package idorm.idormServer.community.postLike.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.postLike.application.port.out.DeletePostLikePort;
import idorm.idormServer.community.postLike.entity.PostLike;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DeletePostLikeAdapter implements DeletePostLikePort {

  private final PostLikeRepository postLikeRepository;

  @Override
  public void delete(PostLike postLike) {
    postLikeRepository.delete(postLike);
  }
}
