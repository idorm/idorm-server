package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.Title;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TitleMapper {

    public TitleEmbeddedEntity toEntity(Title title) {
        return new TitleEmbeddedEntity(title.getValue());
    }

    public Title toDomain(TitleEmbeddedEntity entity) {
        return Title.forMapper(entity.getValue());
    }
}