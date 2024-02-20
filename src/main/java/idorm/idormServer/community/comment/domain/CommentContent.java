package idorm.idormServer.community.comment.domain;

import idorm.idormServer.community.post.domain.Content;
import lombok.Getter;

@Getter
public class CommentContent extends Content {

	private static final int MIN_LENGTH = 1;
	public static final int MAX_LENGTH = 50;

	public CommentContent(final String value) {
		super(value);
	}

	public static CommentContent from(final String value) {
		return new CommentContent(value);
	}

	public static CommentContent forMapper(final String value) {
		return new CommentContent(value);
	}
}