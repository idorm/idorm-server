package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.Content;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentMapper {
    public ContentEmbeddedEntity toEntity(Content content) {


    }

    public Content toDomain(ContentEmbeddedEntity content) {
    }
}
