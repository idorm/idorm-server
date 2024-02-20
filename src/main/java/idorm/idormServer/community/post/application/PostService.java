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
import idorm.idormServer.community.post.domain.Content;
import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.community.post.domain.Title;
import idorm.idormServer.community.postLike.application.port.out.LoadPostLikePort;
import idorm.idormServer.community.postPhoto.application.port.in.PostPhotoUseCase;
import idorm.idormServer.community.postPhoto.application.port.out.DeletePostPhotoPort;
import idorm.idormServer.community.postPhoto.domain.PostPhoto;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.domain.Member;
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

	//  private final SaveFilePort saveFilePort;
	private final DeletePostPhotoPort deletePostPhotoPort;
	private final PostPhotoUseCase postPhotoUseCase;

	@Override
	@Transactional
	public void save(final AuthResponse authResponse, final PostSaveRequest request) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());

		Post post = new Post(member, DormCategory.valueOf(
			request.dormCategory()),
			new Title(request.title()),
			new Content(request.content()),
			request.isAnonymous());
		savePostPort.save(post);

		//    List<String> photoUrl = saveFilePort.savePostPhotoFiles(post, request.files());
		//    postPhotoUseCase.save(post, photoUrl);

		final List<PostPhoto> postPhotos = postPhotoUseCase.findAllByPost(post);
		post.updatePostPhotos(postPhotos);
	}

	@Override
	@Transactional
	public void update(final AuthResponse authResponse, final Long postId, final PostUpdateRequest request) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Post post = loadPostPort.findById(postId);

		post.validatePostPhotoSize(request.files().size()); // 기존에 남은 사진들 개수 + 추가되는 개수
		post.update(member, new Title(request.title()), new Content(request.content()), request.isAnonymous());

		List<String> deletePhotoUrls = getPhotoUrls(post);
		// TODO : Post 리스트에 매핑된 PostPhoto를 먼저 삭제
		request.deletePostPhotoIds().forEach(id -> deletePostPhotoPort.deleteById(id)); // 이후, PostPhoto Entity 삭제
		// TODO: S3 사진 삭제
		// deleteFilePort.deletePostPhotoFiles(deletePhotoUrls);

		// List<String> photoUrls = saveFilePort.savePostPhotoFiles(post, request.files()); // S3 사진 추가

		// PostPhotos Entity 생성
		// List<PostPhoto> postPhotos = photoUrls.stream()
		// 	.map(photoUrl -> postPhotoUseCase.save(post, photoUrl))
		// 	.toList();

		// Post에 추가된 PostPhotos 추가
		// post.addPostPhotos(postPhotos);
	}

	@Override
	@Transactional
	public void delete(final AuthResponse authResponse, final Long postId) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Post post = loadPostPort.findById(postId);

		List<String> deletePhotoUrls = getPhotoUrls(post);

		post.delete(member);
		deletePostPhotoPort.deleteAllByPostId(post.getId());

		// deleteFilePort.deletePostPhotoFiles(deletePhotoUrls);
	}

	@Override
	public List<PostListResponse> findPostsByMember(final AuthResponse authResponse) {
		final List<Post> posts = loadPostPort.findPostsByMemberId(authResponse.getId());
		return PostListResponse.of(posts);
	}

	@Override
	public Page<PostListResponse> findPostsByDormCategory(final String dormCategory, final int pageNum) {
		final Page<Post> posts = loadPostPort.findPostsByDormCategoryAndIsDeletedFalse(
			DormCategory.from(dormCategory), PageRequest.of(pageNum, 10));

		Page<PostListResponse> responses = posts.map(PostListResponse::of);
		return responses;
	}

	@Override
	public List<PostListResponse> findTopPostsByDormCategory(final String dormCategory) {
		final List<Post> posts = loadPostPort.findTopPostsByDormCateogry(DormCategory.from(dormCategory));
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
}