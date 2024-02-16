package idorm.idormServer.auth.service;

import idorm.idormServer.auth.dto.AuthInfo;
import idorm.idormServer.auth.dto.LoginRequest;
import idorm.idormServer.auth.encryptor.EncryptorI;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final EncryptorI encryptor;

    public AuthService(MemberRepository memberRepository, EncryptorI encryptor) {
        this.memberRepository = memberRepository;
        this.encryptor = encryptor;
    }

    public AuthInfo login(LoginRequest request) {
        Member member = memberRepository.findByEmailAndPasswordValueAndMemberStatusIsActive(request.email(),
                        request.password())
                .orElseThrow(() -> new CustomException(null, ExceptionCode.UNAUTHORIZED_MEMBER));

        return new AuthInfo(member.getId(), member.getRoleType().getName(), member.getNickname().getValue());
    }
}
