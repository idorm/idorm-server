package idorm.idormServer.community.domain;

import static idorm.idormServer.common.exception.ExceptionCode.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.util.Validator;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
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
	private String writerNickname;
	private Boolean isDeleted;
	private Member member;
	private List<PostPhoto> postPhotos = new ArrayList<>();
	private List<PostLike> postLikes = new ArrayList<>();
	private List<Comment> comments = new ArrayList<>();
	private List<Report> reports = new ArrayList<>();
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Post(Member member,
		DormCategory dormCategory,
		Title title,
		Content content,
		String writerNickname,
		List<PostPhoto> postPhotos) {
		validate(writerNickname, postPhotos);

		this.dormCategory = dormCategory;
		this.title = title;
		this.content = content;
		this.writerNickname = writerNickname;
		this.isDeleted = false;
		this.member = member;
		this.postPhotos = postPhotos;
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	public static Post forMapper(final Long id,
		final DormCategory dormCategory,
		final Title title,
		final Content content,
		final String writerNickname,
		final Boolean isDeleted,
		final Member member,
		final List<PostPhoto> postPhotos,
		final List<PostLike> postLikes,
		final List<Comment> comments,
		final List<Report> reports,
		final LocalDateTime createdAt,
		final LocalDateTime updatedAt) {

		return new Post(id, dormCategory, title, content, writerNickname, isDeleted, member, postPhotos, postLikes,
			comments, reports, createdAt, updatedAt);
	}

	private void validate(String writerNickname, List<PostPhoto> postPhotos) {
		Validator.validateNotBlank(writerNickname);
		validatePostPhotoSize(postPhotos.size());
	}

	private void validatePostPhotoSize(int count) {
		if (count > MAX_POST_PHOTO_SIZE) {
			throw new CustomException(null, FILE_COUNT_EXCEED);
		}
	}

	public void updateTitle(Title title) {
		this.title = title;
	}

	public void updateContent(Content content) {
		this.content = content;
	}

	public void updateWriterNickname(String nickname) {
		Validator.validateNotBlank(nickname);
		this.writerNickname = nickname;
	}

	public void updatePostPhotos(List<PostPhoto> postPhotos) {
		validatePostPhotoSize(postPhotos.size());
		this.postPhotos.clear();
		this.postPhotos = postPhotos;
	}

	public boolean isOwner(Member member) {
		return this.member.equals(member);
	}

	public boolean isAnonymous() {
		return !getNickname().equals(member.getNickname().getValue());
	}

	public String getNickname() {
		if (isBlocked()) {
			return BLIND_POST_MESSAGE;
		}
		return writerNickname;
	}

	public String getTitleValue() {
		if (isBlocked()) {
			return BLIND_POST_MESSAGE;
		}
		return title.getValue();
	}

	public String getContentValue() {
		if (isBlocked()) {
			return BLIND_POST_MESSAGE;
		}
		return content.getValue();
	}

	public int getCommentCount() {
		if (Objects.isNull(comments)) {
			return 0;
		}
		return comments.size();
	}

	public int getPostLikeCount() {
		if (Objects.isNull(postLikes)) {
			return 0;
		}
		return postLikes.size();
	}

	private boolean isBlocked() {
		return reports.size() >= BLOCKED_CONDITION;
	}

	void addPostLike(PostLike postLike) {
		this.postLikes.add(postLike);
	}

	void deletePostLike(PostLike postLike) {
		this.postLikes.remove(postLike);
	}

	void addComment(Comment comment) {
		this.comments.add(comment);
	}

	void deleteComment(Comment comment) {
		this.comments.remove(comment);
	}

	public void delete() {
		this.isDeleted = true;
		// TODO : postPhotos 및 S3 사진 삭제
	}
}