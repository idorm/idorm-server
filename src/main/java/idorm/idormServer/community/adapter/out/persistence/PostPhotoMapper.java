package idorm.idormServer.community.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.domain.PostPhoto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

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