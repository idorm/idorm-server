package idorm.idormServer.community.domain;

import static idorm.idormServer.common.exception.ExceptionCode.FILE_COUNT_EXCEED;

import idorm.idormServer.common.domain.BaseTimeEntity;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.util.Validator;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.report.domain.Report;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    private static final String BLIND_POST_MESSAGE = "블라인드 처리된 게시글입니다.";
    private static final int BLOCKED_CONDITION = 5;
    private static final int MAX_POST_PHOTO_SIZE = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DORM1', 'DORM2', 'DORM3')")
    private DormCategory dormCategory;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @NotNull
    @Column(nullable = false)
    private String writerNickname;

    @NotNull
    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<PostPhoto> postPhotos = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Report> reports = new ArrayList<>();

    @Builder
    public Post(Member member, DormCategory dormCategory, Title title, Content content, String writerNickname,
                List<PostPhoto> postPhotos) {
        validate(writerNickname, postPhotos);
        this.dormCategory = dormCategory;
        this.title = title;
        this.content = content;
        this.writerNickname = writerNickname;
        this.isDeleted = false;
        this.member = member;
        this.postPhotos = postPhotos;
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

    public String getTitle() {
        if (isBlocked()) {
            return BLIND_POST_MESSAGE;
        }
        return title.getValue();
    }

    public String getContent() {
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