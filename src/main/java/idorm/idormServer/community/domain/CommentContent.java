package idorm.idormServer.community.domain;

public class CommentContent extends Content {

    private static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 50;

    public CommentContent(final String value) {
        super(value);
    }
}