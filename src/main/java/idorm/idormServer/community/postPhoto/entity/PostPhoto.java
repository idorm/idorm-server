package idorm.idormServer.community.postPhoto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.post.adapter.out.exception.AccessDeniedPostException;
import idorm.idormServer.community.post.entity.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhoto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_photo_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	private String photoUrl;

	public PostPhoto(final Post post, final String photoUrl) {
		validate(post, photoUrl);
		this.post = post;
		this.photoUrl = photoUrl;
	}

	public void validate(Post post, String postUrl) {
		Validator.validateNotNull(post);
		Validator.validateNotBlank(postUrl);
	}

	public void validatePostPhotoAccessMember(final Long postId) {
		if (this.getPost().getId() != postId) {
			throw new AccessDeniedPostException();
		}
	}
}