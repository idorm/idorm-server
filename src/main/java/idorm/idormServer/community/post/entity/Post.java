package idorm.idormServer.community.post.entity;

import static idorm.idormServer.community.post.adapter.out.PostResponseCode.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import idorm.idormServer.common.entity.BaseTimeEntity;
import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.comment.entity.Comment;
import idorm.idormServer.community.post.adapter.out.exception.AccessDeniedPostException;
import idorm.idormServer.community.postLike.adapter.out.exception.CannotLikedSelfException;
import idorm.idormServer.community.postLike.entity.PostLike;
import idorm.idormServer.community.postPhoto.entity.PostPhoto;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.member.entity.Member;
import idorm.idormServer.photo.adapter.out.exception.ExceedFileCountException;
import idorm.idormServer.report.entity.Report;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

	private static final String BLIND_POST_MESSAGE = "블라인드 처리된 게시글입니다.";
	private static final int BLOCKED_CONDITION = 5;
	private static final int MIN_LENGTH = 3;
	private static final int MAX_LENGTH_FOR_CONTENT = 300;
	private static final int MAX_LENGTH_FOR_TITLE = 50;
	private static final int MAX_POST_PHOTO_SIZE = 10;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('DORM1', 'DORM2', 'DORM3')")
	private DormCategory dormCategory;

	@Column(name = "title", nullable = false, length = MAX_LENGTH_FOR_TITLE)
	private String title;

	@Column(name = "content", nullable = false, length = MAX_LENGTH_FOR_CONTENT)
	private String content;

	@Column(nullable = false)
	private int likeCount = 0;

	@Column(nullable = false)
	private Boolean isDeleted;

	@Column(nullable = false)
	Boolean isBlocked;

	@Column(nullable = false)
	private Boolean isAnonymous;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PostPhoto> postPhotos = new ArrayList<>();

	@OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PostLike> postLikes = new ArrayList<>();

	@OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "reportedPost", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Report> reports = new ArrayList<>();

	public Post(final Member member,
		final DormCategory dormCategory,
		final String title,
		final String content,
		final Boolean isAnonymous) {
		validateConsturctor(title, content, isAnonymous);
		this.dormCategory = dormCategory;
		this.title = title;
		this.content = content;
		this.isDeleted = false;
		this.isAnonymous = isAnonymous;
		this.member = member;
		this.isBlocked = false;
	}

	private void validateConsturctor(String title, String content, Boolean isAnonymous) {
		validateTitle(title);
		validateContent(content);
		Validator.validateNotNull(isAnonymous);
	}

	public void update(Member member, String title, String content, Boolean isAnonymous) {
		validatePostAuthorization(member);
		validateConsturctor(title, content, isAnonymous);
		this.title = title;
		this.content = content;
		this.isAnonymous = isAnonymous;
	}

	public void addPostPhotos(final List<PostPhoto> postPhotos) {
		this.postPhotos.addAll(postPhotos);
	}

	public void addReport(final Report report){
		this.reports.add(report);
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

	public void addPostPhoto(List<PostPhoto> postPhotos) {
		this.postPhotos.addAll(postPhotos);
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	public void deleteComment(Comment comment) {
		this.comments.remove(comment);
	}

	public void delete(Member member) {
		validatePostAuthorization(member);
		this.isDeleted = true;
		this.postPhotos.clear();
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


	private void validatePostAuthorization(final Member member) {
		if (this.getMember() == null || !this.getMember().equals(member)) {
			throw new AccessDeniedPostException();
		}
	}

	private void validateTitle(final String title) {
		Validator.validateNotBlank(title);
		Validator.validateLength(title, MIN_LENGTH, MAX_LENGTH_FOR_TITLE, INVALID_TITLE_LENGTH);
	}

	private void validateContent(final String content) {
		Validator.validateNotBlank(content);
		Validator.validateLength(content, MIN_LENGTH, MAX_LENGTH_FOR_CONTENT, INVALID_CONTENT_LENGTH);
	}
}