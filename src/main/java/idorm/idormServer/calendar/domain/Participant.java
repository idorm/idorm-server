package idorm.idormServer.calendar.domain;

import idorm.idormServer.common.util.Validator;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "memberId")
public class Participant implements Comparable<Participant> {

	private Long memberId;

	public Participant(final Long memberId) {
		validateConstructor(memberId);
		this.memberId = memberId;
	}

	public static Participant forMapper(final Long memberId) {
		return new Participant(memberId);
	}

	private void validateConstructor(Long memberId) {
		Validator.validateNotNull(memberId);
	}

	@Override
	public int compareTo(Participant other) {
		return this.memberId < other.memberId ? -1 : 1;
	}
}