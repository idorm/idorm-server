package idorm.idormServer.domain;

//import idorm.idormServer.domain.Community.Comment;
//import idorm.idormServer.domain.Community.SubComment;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotBlank;
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

    private String email;

    private String password;
    private String nickname; // 커뮤니티 게시글에선 익명/닉네임 여부 선택 가능, 댓글에선 전부 익명1,2,3

//    @OneToOne(mappedBy = "member")
//    @JoinColumn(name="photo_id")
//    private Photo profileImage; // 프로필 이미지
//
//    @OneToMany
//    private List<Member> likedMem = new ArrayList<>(); // 좋아요한 룸메
//
//    @OneToOne(mappedBy = "member")
//    @JoinColumn(name="matchingInfo_id")
//    private MatchingInfo matchingInfo;
//
//    @OneToOne(mappedBy = "member")
//    @JoinColumn(name = "matching_id")
//    private Matching matching; // 로그인한 멤버를 불러오기 위한
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "matching_id")
//    private Matching matchings; // 매칭되는 멤버들을 불러오기 위한
//
//    @OneToOne(mappedBy = "commentCreator")
//    @JoinColumn(name = "comment_id")
//    private Comment comment;
//
//    @OneToOne(mappedBy = "subCommentCreator")
//    @JoinColumn(name = "subComment_id")
//    private SubComment subComment;

    /**
     * security code
     */
    // 해당 USER의 권한을 return 하는 곳

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

        // 우리 사이트에서 1년동안 회원이 로그인을 안하면 휴면계정으로 하기로 했다면
        // return true;
    }

    /**
     * security code end
     */


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

}
