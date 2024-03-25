package idorm.idormServer.community.post.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.community.comment.application.port.out.LoadCommentPort;
import idorm.idormServer.community.post.application.port.in.PostUseCase;
import idorm.idormServer.community.post.application.port.in.dto.PostListResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostResponse;
import idorm.idormServer.community.post.application.port.in.dto.PostSaveRequest;
import idorm.idormServer.community.post.application.port.in.dto.PostUpdateRequest;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.application.port.out.SavePostPort;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.community.postLike.application.port.out.LoadPostLikePort;
import idorm.idormServer.community.postPhoto.application.port.in.PostPhotoUseCase;
import idorm.idormServer.community.postPhoto.application.port.out.DeletePostPhotoPort;
import idorm.idormServer.community.postPhoto.entity.PostPhoto;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.entity.Member;
import idorm.idormServer.photo.application.port.out.DeleteFilePort;
import idorm.idormServer.photo.application.port.out.SaveFilePort;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService implements PostUseCase {

	private final LoadMemberPort loadMemberPort;

	private final SavePostPort savePostPort;
	private final LoadPostPort loadPostPort;

	private final LoadCommentPort loadCommentPort;

	private final LoadPostLikePort loadPostLikePort;

	private final SaveFilePort saveFilePort;
	private final DeleteFilePort deleteFilePort;
	private final DeletePostPhotoPort deletePostPhotoPort;
	private final PostPhotoUseCase postPhotoUseCase;

	@Override
	@Transactional
	public PostResponse save(final AuthResponse authResponse, final PostSaveRequest request) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		Post post = request.toEntity(member);
		if (request.files() != null) {
			post.validatePostPhotoSize(request.files().size());
			savePostPort.save(post);
			List<String> photoUrl = saveFilePort.addPostPhotoFiles(post, request.files());
			postPhotoUseCase.save(post, photoUrl);

			final List<PostPhoto> postPhotos = postPhotoUseCase.findAllByPost(post);
			post.addPostPhoto(postPhotos);
		}
		savePostPort.save(post);
		return PostResponse.of(post, postPhotoUseCase.findAllByPost(post));
	}

	@Override
	@Transactional
	public void update(final AuthResponse authResponse, final Long postId, final PostUpdateRequest request) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Post post = loadPostPort.findByIdAndMemberId(postId, member.getId());

		if (request.deletePostPhotoIds() != null) {
			handleDeletePostPhotos(post, request);
		}
		if (request.files() != null) {
			handleUpdatePost(member, post, request);
		}
		post.update(member, request.title(), request.content(), request.isAnonymous());
	}

	@Override
	@Transactional
	public void delete(final AuthResponse authResponse, final Long postId) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Post post = loadPostPort.findByIdAndMemberId(postId, member.getId());

		List<String> deletePhotoUrls = getPhotoUrls(post);

		post.delete(member);

		deleteFilePort.deletePostPhotoFiles(deletePhotoUrls);
	}

	@Override
	public List<PostListResponse> findPostsByMember(final AuthResponse authResponse) {
		List<Post> posts = loadPostPort.findPostsByMemberId(authResponse.getId());
		return PostListResponse.of(posts);
	}

	@Override
	public Page<PostListResponse> findPostsByDormCategory(final String dormCategory, final int pageNum) {
		Page<Post> posts = loadPostPort.findPostsByDormCategoryAndIsDeletedFalse(
			DormCategory.from(dormCategory), PageRequest.of(pageNum, 10));

		return posts.map(PostListResponse::new);
	}

	@Override
	public List<PostListResponse> findTopPostsByDormCategory(final String dormCategory) {
		List<Post> posts = loadPostPort.findTopPostsByDormCateogry(
			DormCategory.from(dormCategory));
		return PostListResponse.of(posts);
	}

	@Override
	public PostResponse findOneByPostId(final AuthResponse authResponse, final Long postId) {
		final Post post = loadPostPort.findById(postId);

		return PostResponse.of(post,
			postPhotoUseCase.findAllByPost(post),
			loadCommentPort.findAllByPostId(postId),
			loadPostLikePort.existsByMemberIdAndPostId(authResponse.getId(), post.getId()));
	}

	private List<String> getPhotoUrls(final Post post) {
		List<String> photoUrls = post.getPostPhotos().stream()
			.map(PostPhoto::getPhotoUrl)
			.toList();
		return photoUrls;
	}

	private void handleDeletePostPhotos(Post post, PostUpdateRequest request) {
		final int photoCount =
			post.getPostPhotos().size() - request.deletePostPhotoIds().size() + request.files().size();

		post.validatePostPhotoSize(photoCount);

		List<String> deletePhotoUrls = getPhotoUrls(post);
		deleteFilePort.deletePostPhotoFiles(deletePhotoUrls);

		request.deletePostPhotoIds().forEach(id -> deletePostPhotoPort.deleteById(id));
	}

	private void handleUpdatePost(Member member, Post post, PostUpdateRequest request) {
		post.update(member, request.title(), request.content(), request.isAnonymous());
		List<String> photoUrls = saveFilePort.addPostPhotoFiles(post, request.files());

		List<PostPhoto> postPhotos = photoUrls.stream()
			.map(photoUrl -> new PostPhoto(post, photoUrl))
			.toList();

		post.addPostPhotos(postPhotos);
	}
}