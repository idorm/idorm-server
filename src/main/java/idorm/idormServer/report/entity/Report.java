package idorm.idormServer.report.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import idorm.idormServer.common.exception.InvalidTargetSelfException;
import idorm.idormServer.common.util.Validator;
import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "REPORT_TYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporter_member_id")
	private Member reporterMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_member_id")
	private Member reportedMember;

	private String reason;

	@Column(updatable = false)
	private LocalDateTime createdAt;

	Report(Member reporterMember, Member reportedMember, String reason) {
		Validator.validateNotNull(List.of(reporterMember, reportedMember));
		validateTargetNotSelf(reporterMember, reportedMember);

		this.reporterMember = reporterMember;
		this.reportedMember = reportedMember;
		this.reason = reason;
		this.createdAt = LocalDateTime.now();

		reportedMember.addReport(this);
	}

	private void validateTargetNotSelf(Member reporterMember, Member reportedMember) {
		if (reporterMember.equals(reportedMember)) {
			throw new InvalidTargetSelfException();
		}
	}
}