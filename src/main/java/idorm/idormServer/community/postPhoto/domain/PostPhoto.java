package idorm.idormServer.community.postPhoto.domain;

import idorm.idormServer.community.exception.AccessDeniedPostException;
import java.util.List;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.post.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostPhoto {

	private Long id;
	private Post post;
	private String photoUrl;

	public PostPhoto(final Post post, final String photoUrl) {
		validate(post, photoUrl);
		this.post = post;
		this.photoUrl = photoUrl;

		post.addPostPhoto(this);
	}

	public static List<PostPhoto> of(Post post, List<String> photoUrls) {
		List<PostPhoto> postPhotos = photoUrls.stream()
			.map(photoUrl -> new PostPhoto(post, photoUrl))
			.toList();
		return postPhotos;
	}

	public static PostPhoto forMapper(final Long id, final Post post, final String photoUrl) {
		return new PostPhoto(id, post, photoUrl);
	}

	public void validate(Post post, String postUrl) {
		Validator.validateNotNull(post);
		Validator.validateNotBlank(postUrl);
	}

	public void validatePostPhotoAccessMember(Long postId){
		if(this.getPost().getId()!=postId){
			throw new AccessDeniedPostException();
		}
	}
}