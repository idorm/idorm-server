package idorm.idormServer.calendar.adapter.out.persistence;

import static idorm.idormServer.calendar.domain.Content.MAX_LENGTH;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CalendarContentEmbeddedEntity {

    @Column(name = "content", nullable = false, length = MAX_LENGTH)
    private String value;
}