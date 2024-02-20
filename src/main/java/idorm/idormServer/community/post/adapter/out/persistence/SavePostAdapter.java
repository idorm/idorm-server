package idorm.idormServer.community.post.adapter.out.persistence;

import idorm.idormServer.community.post.application.port.out.SavePostPort;
import idorm.idormServer.community.post.domain.Post;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostAdapter implements SavePostPort {

  private final PostMapper postMapper;
  private final PostRepository postRepository;

  @Override
  public void save(Post post) {
    PostJpaEntity postJpaEntity = postMapper.toEntity(post);
    postRepository.save(postJpaEntity);
  }
}
