package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.Duration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DurationMapper {

    public DurationEmbeddedEntity toEntity(Duration duration) {
        return new DurationEmbeddedEntity(duration.getStartTime(), duration.getEndTime());
    }

    public Duration toDomain(DurationEmbeddedEntity entity) {
        return Duration.forMapper(entity.getStartTime(), entity.getEndTime());
    }
}