package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingInfo {

    @Id
    @Column(name="matching_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private DormInfo dormInfo;

    @Embedded
    private PreferenceInfo preferenceInfo;

    @Embedded
    private TextInfo textInfo;

    @Embedded
    private SharedURL openKakaoLink;

    @Column(nullable = false)
    private Boolean isPublic;

    @Builder
    public MatchingInfo(DormInfo dormInfo,
                        PreferenceInfo preferenceInfo,
                        TextInfo textInfo,
                        SharedURL openKakaoLink,
                        Member member) {
        this.dormInfo = dormInfo;
        this.preferenceInfo = preferenceInfo;
        this.textInfo = textInfo;
        this.openKakaoLink = openKakaoLink;
        this.isPublic = false;
        this.member = member;
    }

    public void updateIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
}