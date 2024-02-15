package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.domain.Content;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class ContentMapper {
	public ContentEmbeddedEntity toEntity(Content content) {
		return new ContentEmbeddedEntity(content.getValue());
	}

	public Content toDomain(ContentEmbeddedEntity content) {
		return Content.forMapper(content.getValue());
	}
}
