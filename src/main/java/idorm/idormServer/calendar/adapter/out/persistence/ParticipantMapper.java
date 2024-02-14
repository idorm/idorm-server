package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.Participant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantMapper {

    public ParticipantEmbeddedEntity toEntity(Participant participant) {
        return new ParticipantEmbeddedEntity(participant.getMemberId());
    }

    public Participant toDomain(ParticipantEmbeddedEntity entity) {
        return Participant.forMapper(entity.getMemberId());
    }
}