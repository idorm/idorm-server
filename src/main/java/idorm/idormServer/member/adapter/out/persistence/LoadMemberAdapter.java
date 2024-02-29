package idorm.idormServer.member.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.adapter.out.exception.LoginFailedException;
import idorm.idormServer.email.adapter.out.exception.DuplicatedEmailException;
import idorm.idormServer.member.adapter.out.exception.DuplicatedNicknameException;
import idorm.idormServer.member.adapter.out.exception.NotFoundMemberException;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.entity.Member;
import idorm.idormServer.member.entity.MemberStatus;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadMemberAdapter implements LoadMemberPort {

	private final MemberRepository memberRepository;

	@Override
	public Member loadMember(final Long memberId) {
		return memberRepository.findByIdAndMemberStatus(memberId, MemberStatus.ACTIVE)
			.orElseThrow(NotFoundMemberException::new);
	}

	@Override
	public Member loadMember(final String email, final String password) {
		return memberRepository.findByEmailAndPasswordValueAndMemberStatus(email, password, MemberStatus.ACTIVE)
			.orElseThrow(LoginFailedException::new);
	}

	@Override
	public Member loadMember(final String email) {
		return memberRepository.findByEmailAndMemberStatus(email, MemberStatus.ACTIVE)
			.orElseThrow(NotFoundMemberException::new);
	}

	@Override
	public void validateUniqueNickname(final String nickname) {
		boolean exists = memberRepository.existsByNicknameValueAndMemberStatus(nickname, MemberStatus.ACTIVE);
		if (exists) {
			throw new DuplicatedNicknameException();
		}
	}

	@Override
	public void validateUniqueEmail(final String email) {
		boolean exists = memberRepository.existsByEmailAndMemberStatus(email, MemberStatus.ACTIVE);
		if (exists) {
			throw new DuplicatedEmailException();
		}
	}
}