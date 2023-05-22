package idorm.idormServer.auth;

import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Optional<Member> foundMember = memberRepository.findById(Long.parseLong(username));

        if (foundMember.isPresent()) {
            if (foundMember.get().getIsDeleted()) {
                throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
            } else {
                return memberRepository.findByIdAndIsDeletedIsFalse(Long.parseLong(username)).get();
            }
        } else {
            return null;
        }
    }
}