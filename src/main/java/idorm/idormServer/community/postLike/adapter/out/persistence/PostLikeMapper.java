package idorm.idormServer.community.postLike.adapter.out.persistence;

import idorm.idormServer.community.postLike.domain.PostLike;
import idorm.idormServer.community.post.adapter.out.persistence.PostMapper;
import idorm.idormServer.member.adapter.out.persistence.MemberMapper;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeMapper {

  private final PostMapper postMapper;
  private final MemberMapper memberMapper;

  public PostLikeJpaEntity toEntity(PostLike postLike) {
    return new PostLikeJpaEntity(postLike.getId(), postMapper.toEntity(postLike.getPost()),
        memberMapper.toEntity(postLike.getMember()));
  }

  public List<PostLikeJpaEntity> toEntity(List<PostLike> postLikes) {
    List<PostLikeJpaEntity> result = postLikes.stream()
        .map(this::toEntity)
        .toList();
    return result;
  }

  public PostLike toDomain(PostLikeJpaEntity entity) {
    return PostLike.forMapper(entity.getId(), postMapper.toDomain(entity.getPostJpaEntity()),
        memberMapper.toDomain(entity.getMember()));
  }

  public List<PostLike> toDomain(List<PostLikeJpaEntity> entities) {
    List<PostLike> result = entities.stream()
        .map(this::toDomain)
        .toList();
    return result;
  }
}