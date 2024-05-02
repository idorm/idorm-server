package idorm.idormServer.calendar.adapter.out.persistence;

import static idorm.idormServer.calendar.entity.QOfficialCalendar.officialCalendar;

import com.querydsl.jpa.impl.JPAQueryFactory;
import idorm.idormServer.calendar.entity.OfficialCalendar;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OfficialCalendarCustomRepositoryImpl implements OfficialCalendarCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OfficialCalendar> findByToday(DormCategory dormCategory) {
        return queryFactory
            .select(officialCalendar)
            .from(officialCalendar)
            .where(officialCalendar.period.startDate.eq(LocalDate.now()))
            .fetch();
    }
}