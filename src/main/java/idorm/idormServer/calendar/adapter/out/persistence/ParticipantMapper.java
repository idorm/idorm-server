package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.domain.Participant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantMapper {

	public ParticipantEmbeddedEntity toEntity(Participant participant) {
		return new ParticipantEmbeddedEntity(participant.getMemberId());
	}

	public Set<ParticipantEmbeddedEntity> toEntity(Set<Participant> participants) {
		Set<ParticipantEmbeddedEntity> result = participants.stream()
			.map(participant -> new ParticipantEmbeddedEntity(participant.getMemberId()))
			.collect(Collectors.toSet());

		return result;
	}

	public Participant toDomain(ParticipantEmbeddedEntity entity) {
		return Participant.forMapper(entity.getMemberId());
	}

	public Set<Participant> toDomain(Set<ParticipantEmbeddedEntity> entities) {
		Set<Participant> result = entities.stream()
			.map(entity -> Participant.forMapper(entity.getMemberId()))
			.collect(Collectors.toSet());

		return result;
	}
}