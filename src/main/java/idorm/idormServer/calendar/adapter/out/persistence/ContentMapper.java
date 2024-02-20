package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.domain.Content;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class ContentMapper {
	public CalendarContentEmbeddedEntity toEntity(Content content) {
		return new CalendarContentEmbeddedEntity(content.getValue());
	}

	public Content toDomain(CalendarContentEmbeddedEntity content) {
		return Content.forMapper(content.getValue());
	}
}
