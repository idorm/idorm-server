package idorm.idormServer.community.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.domain.Content;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ContentMapper {

	public ContentEmbeddedEntity toEntity(Content content) {
		return new ContentEmbeddedEntity(content.getValue());
	}

	public Content toDomain(ContentEmbeddedEntity entity) {
		return Content.forMapper(entity.getValue());
	}
}