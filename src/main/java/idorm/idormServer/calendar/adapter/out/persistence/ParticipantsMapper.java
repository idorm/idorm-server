package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.domain.Participants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantsMapper {

	private final ParticipantMapper participantMapper;

	public ParticipantsEmbeddedEntity toEntity(Participants participants) {
		return new ParticipantsEmbeddedEntity(participantMapper.toEntity(participants.getParticipants()));
	}

	public Participants toDomain(ParticipantsEmbeddedEntity participants) {
		return Participants.forMapper(participantMapper.toDomain(participants.getParticipantEmbeddedEntities()));
	}
}