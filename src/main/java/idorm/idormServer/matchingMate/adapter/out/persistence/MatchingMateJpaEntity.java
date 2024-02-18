package idorm.idormServer.matchingMate.adapter.out.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import idorm.idormServer.matchingMate.domain.MatePreferenceType;
import idorm.idormServer.member.adapter.out.persistence.MemberJpaEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingMateJpaEntity {

	@Id
	@Column(name = "mate_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private MemberJpaEntity member;

	@ManyToOne
	@JoinColumn(name = "target_member_id")
	private MemberJpaEntity targetMember;

	@Enumerated(value = EnumType.STRING)
	@Column(columnDefinition = "ENUM('FAVORITE', 'NONFAVORITE')")
	private MatePreferenceType preferenceType;
}