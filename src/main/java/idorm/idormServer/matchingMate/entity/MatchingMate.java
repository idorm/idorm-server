package idorm.idormServer.matchingMate.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingMate {

	@Id
	@Column(name = "matching_mate_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "target_member_id")
	private Member targetMember;

	@Enumerated(value = EnumType.STRING)
	@Column(columnDefinition = "ENUM('FAVORITE', 'NONFAVORITE')")
	private MatePreferenceType preferenceType;

	private MatchingMate(final Member loginMember, final Member targetMember, final MatePreferenceType preferenceType) {
		validateConstructor(loginMember, targetMember);
		this.member = loginMember;
		this.targetMember = targetMember;
		this.preferenceType = preferenceType;
	}

	public static MatchingMate of(final Member loginMember,
		final Member targetMember,
		final MatePreferenceType preferenceType) {
		return new MatchingMate(loginMember, targetMember, preferenceType);
	}

	private static void validateConstructor(Member loginMember, Member targetMember) {
		Validator.validateNotNull(List.of(loginMember, targetMember));
	}
}