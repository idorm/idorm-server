package idorm.idormServer.member.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.adapter.out.api.exception.LoginFailedException;
import idorm.idormServer.member.adapter.out.exception.NotFoundMemberException;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadMemberAdapter implements LoadMemberPort {

	private final MemberRepository memberRepository;
	private final MemberMapper memberMapper;

	@Override
	public Member loadMember(final Long memberId) {
		MemberJpaEntity memberJpaEntity = memberRepository.findByIdAndMemberStatusIsActive(memberId)
			.orElseThrow(NotFoundMemberException::new);

		return memberMapper.toDomain(memberJpaEntity);
	}

	@Override
	public Member loadMember(final String email, final String password) {

		MemberJpaEntity memberJpaEntity = memberRepository.findByEmailAndPasswordValueAndMemberStatusIsActive(email,
				password)
			.orElseThrow(LoginFailedException::new);

		return memberMapper.toDomain(memberJpaEntity);
	}

	@Override
	public Member loadMember(final String email) {
		MemberJpaEntity memberJpaEntity = memberRepository.findByEmailAndMemberStatusIsActive(email)
			.orElseThrow(NotFoundMemberException::new);

		return memberMapper.toDomain(memberJpaEntity);
	}
}