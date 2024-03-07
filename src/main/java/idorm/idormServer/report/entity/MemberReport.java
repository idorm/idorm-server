package idorm.idormServer.report.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("M")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReport extends Report {

	@Enumerated(EnumType.STRING)
	private MemberReason reasonType;

	public MemberReport(final Member reporterMember, final Member reportedMember, final MemberReason reasonType,
		final String reason) {
		super(reporterMember, reportedMember, reason);
		this.reasonType = reasonType;
	}
}