package idorm.idormServer.matchingInfo.adapter.out.persistence;

import idorm.idormServer.member.adapter.out.persistence.MemberJpaEntity;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingInfoJpaEntity {

    @Id
    @Column(name="matching_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private MemberJpaEntity member;

    @Embedded
    private DormInfoEmbeddedEntity dormInfo;

    @Embedded
    private PreferenceInfoEmbeddedEntity preferenceInfo;

    @Embedded
    private TextInfoEmbeddedEntity textInfo;

    @Embedded
    private SharedURLEmbeddedEntity sharedURL;

    @Column(nullable = false)
    private Boolean isPublic;

    @Builder
    public MatchingInfoJpaEntity(final Long id,
                                 final MemberJpaEntity member,
                                 final DormInfoEmbeddedEntity dormInfo,
                                 final PreferenceInfoEmbeddedEntity preferenceInfo,
                                 final TextInfoEmbeddedEntity textInfo,
                                 final SharedURLEmbeddedEntity sharedURL,
                                 final Boolean isPublic) {
        this.id = id;
        this.member = member;
        this.dormInfo = dormInfo;
        this.preferenceInfo = preferenceInfo;
        this.textInfo = textInfo;
        this.sharedURL = sharedURL;
        this.isPublic = isPublic;
    }
}