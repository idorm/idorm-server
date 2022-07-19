package idorm.idormServer.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="myinfo_id")
    private MyInfo myInfo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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

    /**
     * 비지니스 로직
     */
    public void updatePassword(String password) {
        this.password = password;
    }

}
