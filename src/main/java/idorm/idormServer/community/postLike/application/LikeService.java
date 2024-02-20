package idorm.idormServer.community.postLike.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostListResponse;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.community.postLike.application.port.in.LikeUseCase;
import idorm.idormServer.community.postLike.application.port.out.DeletePostLikePort;
import idorm.idormServer.community.postLike.application.port.out.LoadPostLikePort;
import idorm.idormServer.community.postLike.application.port.out.SavePostLikePort;
import idorm.idormServer.community.postLike.domain.PostLike;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService implements LikeUseCase {

	private final SavePostLikePort savePostLikePort;
	private final DeletePostLikePort deletePostLikePort;
	private final LoadPostLikePort loadPostLikePort;

	private final LoadPostPort loadPostPort;

	private final LoadMemberPort loadMemberPort;

	@Override
	@Transactional
	public void save(final AuthResponse authResponse, final Long postId) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Post post = loadPostPort.findById(postId);

		post.validateNotWriter(member);
		loadPostLikePort.validateExists(member, post);

		PostLike postLike = new PostLike(post, member);
		savePostLikePort.save(postLike);
	}

	@Override
	@Transactional
	public void delete(final AuthResponse authResponse, final Long postId) {
		final Post post = loadPostPort.findById(postId);
		final PostLike postLike = loadPostLikePort.findByMemberIdAndPostId(authResponse.getId(), postId);

		post.deletePostLike(postLike);
		deletePostLikePort.delete(postLike);
	}

	@Override
	public List<PostListResponse> findLikedPostsByMember(final AuthResponse authResponse) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final List<Post> posts = loadPostPort.findLikePostsByMemberId(member.getId());

		return PostListResponse.of(posts);
	}
}
