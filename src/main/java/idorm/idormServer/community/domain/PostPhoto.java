package idorm.idormServer.community.domain;

import java.util.ArrayList;
import java.util.List;

import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostPhoto {

	private Long id;
	private Post post;
	private String photoUrl;

	private PostPhoto(final Post post, final String photoUrl) {
		validateConstructor(post, photoUrl);
		this.post = post;
		this.photoUrl = photoUrl;
	}

	public static List<PostPhoto> of(Post post, List<String> photoUrls) {
		List<PostPhoto> postPhotos = new ArrayList<>();

		photoUrls.forEach(photoUrl -> postPhotos.add(new PostPhoto(post, photoUrl)));
		return postPhotos;
	}

	public static PostPhoto forMapper(final Long id, final Post post, final String photoUrl) {
		return new PostPhoto(id, post, photoUrl);
	}

	private void validateConstructor(Post post, String postUrl) {
		Validator.validateNotNull(post);
		Validator.validateNotBlank(postUrl);
	}
}