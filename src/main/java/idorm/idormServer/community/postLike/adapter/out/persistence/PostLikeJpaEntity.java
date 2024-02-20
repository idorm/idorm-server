package idorm.idormServer.community.postLike.adapter.out.persistence;

import idorm.idormServer.community.post.adapter.out.persistence.PostJpaEntity;
import idorm.idormServer.member.adapter.out.persistence.MemberJpaEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_like_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private PostJpaEntity postJpaEntity;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private MemberJpaEntity member;
}