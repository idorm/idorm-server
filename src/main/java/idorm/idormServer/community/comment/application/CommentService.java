package idorm.idormServer.community.comment.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.community.comment.application.port.in.CommentUseCase;
import idorm.idormServer.community.comment.application.port.in.dto.CommentRequest;
import idorm.idormServer.community.comment.application.port.in.dto.CommentResponse;
import idorm.idormServer.community.comment.application.port.out.LoadCommentPort;
import idorm.idormServer.community.comment.application.port.out.SaveCommentPort;
import idorm.idormServer.community.comment.entity.Comment;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService implements CommentUseCase {

	private final LoadMemberPort loadMemberPort;

	private final LoadPostPort loadPostPort;

	private final SaveCommentPort saveCommentPort;
	private final LoadCommentPort loadCommentPort;

	@Override
	@Transactional
	public void save(final AuthResponse authResponse, final Long postId, final CommentRequest request) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Post post = loadPostPort.findById(postId);

		Comment comment = null;
		if (request.isParent()) {
			comment = createParentComment(member, post, request);
		} else {
			comment = createChildComment(member, post, request);
		}
		saveCommentPort.save(comment);
	}

	@Override
	@Transactional
	public void delete(final AuthResponse authResponse, final Long postId, final Long commentId) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Comment comment = loadCommentPort.findOneByCommentIdAndPostId(commentId, postId);
		comment.delete(member);
	}

	@Override
	public List<CommentResponse> findAllByMember(final AuthResponse authResponse) {
		List<Comment> comments = loadCommentPort.findAllByMemberId(authResponse.getId());

		return CommentResponse.from(comments);
	}

	private Comment createParentComment(final Member member, final Post post, final CommentRequest request) {
		return Comment.parent(request.content(), request.isAnonymous(), post, member);
	}

	private Comment createChildComment(final Member member, final Post post, final CommentRequest request) {
		final Comment comment = loadCommentPort.findById(request.parentCommentId());

		return Comment.child(request.isAnonymous(), request.content(), comment, post, member);
	}
}