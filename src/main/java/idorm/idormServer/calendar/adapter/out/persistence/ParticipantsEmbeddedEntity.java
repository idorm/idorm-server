package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantsEmbeddedEntity {

    @ElementCollection
    @CollectionTable(name = "participantEmbeddedEntities", joinColumns = @JoinColumn(name = "team_calendar_id"))
    private Set<ParticipantEmbeddedEntity> participantEmbeddedEntities = new HashSet<>();
}