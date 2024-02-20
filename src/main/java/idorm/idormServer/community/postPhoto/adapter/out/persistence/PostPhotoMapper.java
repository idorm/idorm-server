package idorm.idormServer.community.postPhoto.adapter.out.persistence;

import idorm.idormServer.community.post.adapter.out.persistence.PostMapper;
import idorm.idormServer.community.postPhoto.domain.PostPhoto;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhotoMapper {

  private final PostMapper postMapper;

  public PostPhotoJpaEntity toEntity(PostPhoto postPhoto) {
    return new PostPhotoJpaEntity(postPhoto.getId(),
        postMapper.toEntity(postPhoto.getPost()),
        postPhoto.getPhotoUrl());
  }

  public List<PostPhotoJpaEntity> toEntity(List<PostPhoto> postPhotos) {
    List<PostPhotoJpaEntity> result = postPhotos.stream()
        .map(this::toEntity)
        .toList();
    return result;
  }

  public PostPhoto toDomain(PostPhotoJpaEntity entity) {
    return PostPhoto.forMapper(entity.getId(), postMapper.toDomain(entity.getPostJpaEntity()),
        entity.getPhotoUrl());
  }

  public List<PostPhoto> toDomain(List<PostPhotoJpaEntity> entities) {
    List<PostPhoto> result = entities.stream()
        .map(this::toDomain)
        .toList();
    return result;
  }
}