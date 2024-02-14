package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.Title;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TitleEmbeddedEntity {

    @Column(name = "title", nullable = false, length = Title.MAX_OFFICIAL_CALENDAR_LENGTH)
    private String value;
}