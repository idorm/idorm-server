package idorm.idormServer.calendar.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

import idorm.idormServer.calendar.adapter.out.exception.DuplicatedMemberException;
import idorm.idormServer.member.adapter.out.exception.NotFoundMemberException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participants {

	@ElementCollection
	@CollectionTable(name = "participant", joinColumns = @JoinColumn(name = "team_calendar_id"))
	private Set<Participant> participants = new HashSet<>();

	public Participants(final Set<Participant> participants) {
		this.participants = participants;
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

	List<Participant> getParticipants() {
		List<Participant> responses = participants.stream()
			.sorted()
			.toList();
		return responses;
	}
}