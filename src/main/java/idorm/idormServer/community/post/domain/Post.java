package idorm.idormServer.community.post.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.comment.domain.Comment;
import idorm.idormServer.community.exception.AccessDeniedPostException;
import idorm.idormServer.community.exception.CannotLikedSelfException;
import idorm.idormServer.community.postLike.domain.PostLike;
import idorm.idormServer.community.postPhoto.domain.PostPhoto;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.adapter.out.api.exception.ExceedFileCountException;
import idorm.idormServer.report.domain.Report;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Post {

	private static final String BLIND_POST_MESSAGE = "블라인드 처리된 게시글입니다.";
	private static final int BLOCKED_CONDITION = 5;
	private static final int MAX_POST_PHOTO_SIZE = 10;

	private Long id;
	private DormCategory dormCategory;
	private Title title;
	private Content content;
	private int likeCount = 0;
	private Boolean isDeleted;
	private Boolean isBlocked;
	private Boolean isAnonymous;
	private Member member;
	private List<PostPhoto> postPhotos = new ArrayList<>();
	private List<PostLike> postLikes = new ArrayList<>();
	private List<Comment> comments = new ArrayList<>();
	private List<Report> reports = new ArrayList<>();
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Post(final Member member,
		final DormCategory dormCategory,
		final Title title,
		final Content content,
		final Boolean isAnonymous,
		final int likeCount,
		final List<PostPhoto> postPhotos) {
		validate(title, content);
		this.dormCategory = dormCategory;
		this.title = title;
		this.content = content;
		this.isBlocked = false;
		this.isDeleted = false;
		this.likeCount = likeCount;
		this.isAnonymous = isAnonymous;
		this.member = member;
		this.postPhotos = postPhotos;
/*    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();*/
	}

	public Post(final Member member,
		final DormCategory dormCategory,
		final Title title,
		final Content content,
		final Boolean isAnonymous) {
		validate(title, content);
		validateIsAnonymous();
		this.dormCategory = dormCategory;
		this.title = title;
		this.content = content;
		this.isDeleted = false;
		this.isAnonymous = isAnonymous;
		this.member = member;
/*    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();*/
	}

	public static Post forMapper(final Long id,
		final DormCategory dormCategory,
		final Title title,
		final Content content,
		final int likeCount,
		final Boolean isBlocked,
		final Boolean isDeleted,
		final Boolean isAnonymous,
		final Member member,
		final List<PostPhoto> postPhotos,
		final List<PostLike> postLikes,
		final List<Comment> comments,
		final List<Report> reports,
		final LocalDateTime createdAt,
		final LocalDateTime updatedAt) {

		return new Post(id, dormCategory, title, content, likeCount, isBlocked,
			isDeleted, isAnonymous, member, postPhotos, postLikes, comments, reports, createdAt,
			updatedAt);
	}

	public void update(Member member, Title title, Content content, Boolean isAnonymous) {
		validatePostAuthorization(member);
		validate(title, content);
		this.title = title;
		this.content = content;
		this.isAnonymous = isAnonymous;
		// updatePostPhotos(postPhotos);
	}

	public void updatePostPhotos(final List<PostPhoto> postPhotos) {
		validatePostPhotoSize(postPhotos.size());
		this.postPhotos.clear();
		this.postPhotos = postPhotos;
	}

	public void addPostPhotos(final List<PostPhoto> postPhotos) {
		this.postPhotos.addAll(postPhotos);
	}

	private boolean isBlocked() {
		return reports.size() >= BLOCKED_CONDITION;
	}

	public void addPostLike(final PostLike postLike) {
		this.postLikes.add(postLike);
		this.likeCount = postLikes.size();
	}

	public void deletePostLike(final PostLike postLike) {
		if (this.postLikes.contains(postLike)) {
			this.postLikes.remove(postLike);
			this.likeCount = postLikes.size();
		}
	}

	public void addPostPhoto(PostPhoto postPhoto) {
		this.postPhotos.add(postPhoto);
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	public void deleteComment(Comment comment) {
		this.comments.remove(comment);
	}

	public void deletePostPhoto(List<PostPhoto> postPhotos) {
		this.postPhotos.remove(postPhotos);
	}

	public void delete(Member member) {
		validatePostAuthorization(member);
		this.isDeleted = true;
		this.postPhotos = null;
	}

	public void validateNotWriter(final Member member) {
		if (this.member.equals(member)) {
			throw new CannotLikedSelfException();
		}
	}

	public void validatePostPhotoSize(int count) {
		if (count > MAX_POST_PHOTO_SIZE) {
			throw new ExceedFileCountException();
		}
	}

	void validateIsAnonymous() {
		Validator.validateNotNull(this.isAnonymous);
	}

	private void validate(Title title, Content content) {
		this.title.validate(title.getValue());
		this.content.validate(content.getValue());
	}

	private void validatePostAuthorization(Member member) {
		if (this.getMember() == null || !this.getMember().equals(member.getId())) {
			throw new AccessDeniedPostException();
		}
	}

}