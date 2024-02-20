package idorm.idormServer.calendar.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import idorm.idormServer.calendar.adapter.out.exception.FieldTargetsRequiredException;
import idorm.idormServer.member.adapter.out.exception.DuplicatedMemberException;
import idorm.idormServer.member.adapter.out.exception.NotFoundMemberException;
import lombok.Getter;

@Getter
public class Participants {

	private Set<Participant> participants = new HashSet<>();

	public Participants(final Set<Participant> participants) {
		this.participants = participants;
	}

	public static Participants forMapper(final Set<Participant> participants) {
		return new Participants(participants);
	}

	void participate(Long memberId) {
		if (contains(memberId)) {
			throw new DuplicatedMemberException();
		}
		participants.add(new Participant(memberId));
	}

	void delete(Long memberId) {
		if (!this.participants.contains(new Participant(memberId))) {
			throw new NotFoundMemberException();
		}
		participants.remove(new Participant(memberId));
	}

	boolean contains(final Long memberId) {
		return participants.contains(new Participant(memberId));
	}

	public void validateTargetExistence(List<Long> participants) {
		if (participants.size() < 1)
			throw new FieldTargetsRequiredException();
	}

	public List<Participant> getParticipants() {
		List<Participant> responses = participants.stream()
			.sorted()
			.toList();
		return responses;
	}
}