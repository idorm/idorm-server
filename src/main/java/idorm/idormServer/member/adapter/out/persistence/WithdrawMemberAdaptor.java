package idorm.idormServer.member.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.member.application.port.out.WithdrawMemberPort;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WithdrawMemberAdaptor implements WithdrawMemberPort {

	private final MemberRepository memberRepository;
	private final MemberMapper memberMapper;

	@Override
	public void withdraw(final Member member) {
		memberRepository.delete(memberMapper.toEntity(member));
		member.withdraw();
	}
}
