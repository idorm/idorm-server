package idorm.idormServer.auth;

import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("UserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Member foundMember = memberRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
                });

        if (foundMember.getIsDeleted())
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");

        return foundMember;
    }
}