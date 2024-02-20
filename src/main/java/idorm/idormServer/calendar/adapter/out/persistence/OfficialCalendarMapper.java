package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendarMapper {

  private final PeriodMapper periodMapper;
  private final TitleMapper titleMapper;

  public OfficialCalendarJpaEntity toEntity(OfficialCalendar officialCalendar) {
    return new OfficialCalendarJpaEntity(
        officialCalendar.getId(),
        officialCalendar.getIsDorm1Yn(),
        officialCalendar.getIsDorm2Yn(),
        officialCalendar.getIsDorm3Yn(),
        periodMapper.toEntity(officialCalendar.getPeriod()),
        titleMapper.toEntity(officialCalendar.getTitle()),
        officialCalendar.getIsPublic(),
        officialCalendar.getInuPostId(),
        officialCalendar.getInuPostUrl(),
        officialCalendar.getInuPostCreatedAt(),
        officialCalendar.getIsDeleted()
    );
  }


  public List<OfficialCalendarJpaEntity> toEntity(List<OfficialCalendar> officialCalendars) {
    List<OfficialCalendarJpaEntity> results = officialCalendars.stream()
        .map(this::toEntity)
        .toList();
    return results;
  }

  public OfficialCalendar toDomain(OfficialCalendarJpaEntity entity) {
    return OfficialCalendar.forMapper(
        entity.getId(),
        entity.getIsDorm1Yn(),
        entity.getIsDorm2Yn(),
        entity.getIsDorm3Yn(),
        periodMapper.toDomain(entity.getPeriod()),
        titleMapper.toDomain(entity.getTitle()),
        entity.getIsPublic(),
        entity.getInuPostId(),
        entity.getInuPostUrl(),
        entity.getInuPostCreatedAt(),
        entity.getIsDeleted(),
        entity.getCreatedAt(),
        entity.getUpdatedAt()
    );
  }

  public List<OfficialCalendar> toDomain(List<OfficialCalendarJpaEntity> entities) {
    List<OfficialCalendar> results = entities.stream()
        .map(this::toDomain)
        .toList();
    return results;
  }
}
