package idorm.idormServer.report.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.community.comment.entity.Comment;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.entity.Member;
import idorm.idormServer.report.application.port.in.ReportUseCase;
import idorm.idormServer.report.application.port.in.dto.ReportRequest;
import idorm.idormServer.report.application.port.out.SaveReportPort;
import idorm.idormServer.report.entity.Report;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService implements ReportUseCase {

	private LoadMemberPort loadMemberPort;
	private SaveReportPort saveReportPort;

	// TODO: 커뮤니티 코드 반영 후 수정
	// private LoadPostPort loadPostPort;

	@Override
	@Transactional
	public void reportMember(final AuthResponse auth, final ReportRequest request) {
		final Member loginMember = loadMemberPort.loadMember(auth.getId());
		Report memberReport = request.toMemberReportDomain(loginMember,
			loadMemberPort.loadMember(request.targetId()));

		saveReportPort.save(memberReport);
	}

	@Override
	@Transactional
	public void reportPost(final AuthResponse auth, final ReportRequest request) {
		final Member loginMember = loadMemberPort.loadMember(auth.getId());
		// TODO: 커뮤니티 코드 반영 후 수정
		final Post post = null;
		Report postReport = request.toPostReportDomain(loginMember, post);

		saveReportPort.save(postReport);
	}

	@Override
	@Transactional
	public void reportComment(final AuthResponse auth, final ReportRequest request) {
		final Member loginMember = loadMemberPort.loadMember(auth.getId());
		// TODO: 커뮤니티 코드 반영 후 수정
		final Comment comment = null;
		Report commentReport = request.toCommentReportDomain(loginMember, comment);

		saveReportPort.save(commentReport);
	}
}