package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.Participant;
import idorm.idormServer.calendar.domain.Participants;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantsMapper {
    public ParticipantsEmbeddedEntity toEntity(Participants participants) {
        return new ParticipantsEmbeddedEntity(convertParticipantsEntity(participants.getParticipants()));
    }

    public Participants toDomain(ParticipantsEmbeddedEntity participants) {
        return Participants.forMapper(convertParticipants(participants.getParticipantEmbeddedEntities()));
    }

    private Set<ParticipantEmbeddedEntity> convertParticipantsEntity(Set<Participant> participants) {
        Set<ParticipantEmbeddedEntity> result = participants.stream()
                .map(participant -> new ParticipantEmbeddedEntity(participant.getMemberId()))
                .collect(Collectors.toSet());

        return result;
    }

    private Set<Participant> convertParticipants(Set<ParticipantEmbeddedEntity> entities) {
        Set<Participant> result = entities.stream()
                .map(entity -> Participant.forMapper(entity.getMemberId()))
                .collect(Collectors.toSet());

        return result;
    }
}