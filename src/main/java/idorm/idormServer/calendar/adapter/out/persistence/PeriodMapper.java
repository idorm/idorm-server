package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.Period;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PeriodMapper {

    public PeriodEmbeddedEntity toEntity(Period period) {
        return new PeriodEmbeddedEntity(period.getStartDate(), period.getEndDate());
    }

    public Period toDomain(PeriodEmbeddedEntity entity) {
        return Period.forMapper(entity.getStartDate(), entity.getEndDate());
    }
}