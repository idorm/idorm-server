package idorm.idormServer.community.application;

import org.springframework.stereotype.Service;

import idorm.idormServer.community.application.port.in.CommentUseCase;
import idorm.idormServer.community.application.port.in.dto.CommentRequest;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentUseCase {

	@Override
	public void save(Member member, Long postId, CommentRequest request) {

	}

	@Override
	public void delete(Member member, Long postId, Long commentId) {

	}

	@Override
	public void findCommentsByMember(Member member) {

	}
}