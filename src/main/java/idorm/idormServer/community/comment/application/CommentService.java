package idorm.idormServer.community.comment.application;

import static idorm.idormServer.notification.entity.FcmChannel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import idorm.idormServer.notification.adapter.out.event.NotificationClient;
import idorm.idormServer.notification.adapter.out.event.NotificationRequest;
import idorm.idormServer.notification.application.port.out.LoadFcmTokenPort;
import idorm.idormServer.notification.entity.FcmChannel;
import idorm.idormServer.notification.entity.FcmToken;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService implements CommentUseCase {

	private final LoadMemberPort loadMemberPort;
	private final LoadFcmTokenPort loadFcmTokenPort;

	private final LoadPostPort loadPostPort;

	private final SaveCommentPort saveCommentPort;
	private final LoadCommentPort loadCommentPort;

	private final NotificationClient notificationClient;

	@Override
	@Transactional
	public void save(final AuthResponse authResponse, final Long postId, final CommentRequest request) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Post post = loadPostPort.findById(postId);

		Comment comment = null;
		List<NotificationRequest> notificationRequests = new ArrayList<>();

		if (request.isParent()) {
			comment = createParentComment(member, post, request);

			if (comment.isNotOwner(post.getMember())) {
				notificationRequests.add(notificationRequestOf(COMMENT, post.getMember(), comment));
			}
		} else {
			final Comment parentComment = loadCommentPort.findById(request.parentCommentId());
			comment = createChildComment(member, post, parentComment, request);

			if (comment.isNotOwner(post.getMember())) {
				notificationRequests.add(notificationRequestOf(SUBCOMMENT, post.getMember(), comment));
			}
			if (comment.isNotOwner(parentComment.getMember()) && parentComment.isNotOwner(post.getMember())) {
				notificationRequests.add(notificationRequestOf(SUBCOMMENT, parentComment.getMember(), comment));
			}
		}
		saveCommentPort.save(comment);
		notificationClient.notify(notificationRequests);
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

	private Comment createChildComment(final Member member, final Post post, final Comment parentComment,
		final CommentRequest request) {

		return Comment.child(request.isAnonymous(), request.content(), parentComment, post, member);
	}

	private NotificationRequest notificationRequestOf(FcmChannel fcmChannel, Member target, Comment comment) {
		Optional<FcmToken> fcmToken = loadFcmTokenPort.load(target.getId());
		if (fcmToken.isEmpty()) {
			return null;
		}

		NotificationRequest request = NotificationRequest.builder()
			.token(fcmToken.get().getValue())
			.message(NotificationRequest.NotificationMessage.builder()
				.notifyType(fcmChannel)
				.contentId(comment.getId())
				.title(fcmChannel.getTitle())
				.content(comment.getContent())
				.build())
			.build();
		return request;
	}
}