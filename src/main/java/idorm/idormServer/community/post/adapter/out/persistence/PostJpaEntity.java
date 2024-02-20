package idorm.idormServer.community.post.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

import idorm.idormServer.common.domain.BaseTimeEntity;
import idorm.idormServer.community.comment.adapter.out.persistence.CommentJpaEntity;
import idorm.idormServer.community.postLike.adapter.out.persistence.PostLikeJpaEntity;
import idorm.idormServer.community.postPhoto.adapter.out.persistence.PostPhotoJpaEntity;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.adapter.out.persistence.MemberJpaEntity;
import idorm.idormServer.report.adapter.out.persistence.ReportJpaEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('DORM1', 'DORM2', 'DORM3')")
	private DormCategory dormCategory;

	@Embedded
	private TitleEmbeddedEntity title;

	@Embedded
	private ContentEmbeddedEntity content;

	@Column(nullable = false)
	private int likeCount = 0;

	@Column(nullable = false)
	private Boolean isDeleted;

	@Column(nullable = false)
	Boolean isBlocked;

	@Column(nullable = false)
	private Boolean isAnonymous;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberJpaEntity member;

	@OneToMany(mappedBy = "post")
	private List<PostPhotoJpaEntity> postPhotos = new ArrayList<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<PostLikeJpaEntity> postLikes = new ArrayList<>();

	@OneToMany(mappedBy = "post")
	private List<CommentJpaEntity> comments = new ArrayList<>();

	@OneToMany(mappedBy = "post")
	private List<ReportJpaEntity> reports = new ArrayList<>();
}