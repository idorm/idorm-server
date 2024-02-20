package idorm.idormServer.community.post.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.post.domain.Content;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ContentMapper {

	ContentEmbeddedEntity toEntity(Content content) {
		return new ContentEmbeddedEntity(content.getValue());
	}

	Content toDomain(ContentEmbeddedEntity entity) {
		return Content.forMapper(entity.getValue());
	}
}