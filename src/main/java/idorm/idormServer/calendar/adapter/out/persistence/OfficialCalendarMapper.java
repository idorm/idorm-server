package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.OfficialCalendar;

public class OfficialCalendarMapper {

    public OfficialCalendarJpaEntity toEntity(OfficialCalendar officialCalendar) {
        return new OfficialCalendarJpaEntity();
    }

    public OfficialCalendar toDomain(OfficialCalendarJpaEntity entity) {
        return OfficialCalendar.forMapper();
    }
}
