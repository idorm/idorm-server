package idorm.idormServer.member.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.photo.domain.Photo;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity implements UserDetails {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String nickname;

    /**
     * 연관관계 매핑
     */
    @OneToOne(mappedBy = "member")
    private MatchingInfo matchingInfo; // 매칭 정보

    @OneToOne(mappedBy = "member")
    private Photo photo; // 프로필 사진

//    @Setter
//    @ManyToOne
//    private Member loginMember; // 로그인 멤버



//    @Setter
//    @OneToMany(mappedBy = "loginMember", targetEntity = Member.class, fetch = FetchType.LAZY,
//    cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    private List<Member> likedMembers = new ArrayList<>(); // 좋아요한 멤버들
//
//    @Setter
//    @OneToMany(mappedBy = "loginMember")
//    private List<Member> dislikedMembers = new ArrayList<>(); // 싫어요한 멤버들

    /**
     * security code
     */

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    /**
     * security code end
     */

    @Builder
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.roles.add("ROLE_USER");
    }

    /**
     * 비지니스 로직
     */
    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateMatchingInfo(MatchingInfo matchingInfo) {
        this.matchingInfo = matchingInfo;
    }

    public void deleteMatchingInfo() {
        this.matchingInfo = null;
    }


//    public void addLikedMember(Member likedMember) {
//        this.likedMembers.add(likedMember);
//        this.loginMember.setLikedMembers(this.likedMembers);
//    }
//
//    public void deleteLikedMember(Member dislikedMember) {
//        this.likedMembers.remove(dislikedMember);
//        this.loginMember.setLikedMembers(this.likedMembers);
//    }

//    public void addDislikedMember(Member member) {
//        this.dislikedMembers.add(member);
//    }

    public void updatePhoto(Photo photo) {
        this.photo = photo;
    }

}