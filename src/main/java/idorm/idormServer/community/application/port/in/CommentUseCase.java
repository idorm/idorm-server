package idorm.idormServer.community.application.port.in;

import idorm.idormServer.community.application.port.in.dto.CommentRequest;
import idorm.idormServer.member.domain.Member;

public interface CommentUseCase {
	void save(Member member, Long postId, CommentRequest request);

	void delete(Member member, Long postId, Long commentId);

	void findCommentsByMember(Member member);
}
