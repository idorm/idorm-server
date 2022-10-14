package idorm.idormServer.community.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.domain.Photo;
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
public class Post extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name="post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 게시글 작성자

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private List<Photo> photos = new ArrayList<>(); // 업로드 사진들

    private String dormNum; // 기숙사 분류 (커뮤니티 카테고리)
    private String title; // 제목
    private String content; // 내용
    private Boolean isVisible; // 게시글 공개 여부, 삭제 시 false로 변경

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private List<Member> likes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Member member, String dormNum, String title, String content) {
        this.member = member;
        this.dormNum = dormNum;
        this.title = title;
        this.content = content;
        this.isVisible = true;
    }
}
