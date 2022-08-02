package idorm.idormServer.domain.Community;

import idorm.idormServer.domain.Dormitory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DormCategory {

    @Id
    @GeneratedValue
    @Column(name="dormCategory_id")
    private Long id;

    private Dormitory dormNum; // 1기숙사, 2기숙사, 3기숙사

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private List<Post> posts = new ArrayList<>(); // 기숙사별 전체 게시글

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private List<Post> topPosts = new ArrayList<>(); // 기숙사별 인기 게시글

}
