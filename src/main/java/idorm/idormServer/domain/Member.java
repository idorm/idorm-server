package idorm.idormServer.domain;

import idorm.idormServer.domain.Community.Comment;
import idorm.idormServer.domain.Community.SubComment;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
    private String nickname;

    @OneToOne(mappedBy = "member")
    @JoinColumn(name="photo_id")
    private Photo profileImage; // 프로필 이미지

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name="member_id")
//    private List<Member> likedMem; // 좋아요한 룸메

    @OneToOne(mappedBy = "member")
    @JoinColumn(name="matchingInfo_id")
    private MatchingInfo matchingInfo;

    @OneToOne(mappedBy = "member")
    @JoinColumn(name = "matching_id")
    private Matching matching; // 로그인한 멤버를 불러오기 위한

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_id")
    private Matching matchings; // 매칭되는 멤버들을 불러오기 위한

    @OneToOne(mappedBy = "commentCreator")
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @OneToOne(mappedBy = "subCommentCreator")
    @JoinColumn(name = "subComment_id")
    private SubComment subComment;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


    /**
     * 비지니스 로직
     */
    public void updatePassword(String password) {
        this.password = password;
    }

}
