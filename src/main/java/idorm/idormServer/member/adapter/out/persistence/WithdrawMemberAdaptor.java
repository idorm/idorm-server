package idorm.idormServer.member.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.member.application.port.out.WithdrawMemberPort;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WithdrawMemberAdaptor implements WithdrawMemberPort {

	private final MemberRepository memberRepository;

	@Override
	public void withdraw(final Member member) {
		memberRepository.delete(member);
		member.withdraw();
	}
}
