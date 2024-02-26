package idorm.idormServer.calendar.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "memberId")
public class Participant implements Comparable<Participant> {

	@Column(nullable = false)
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