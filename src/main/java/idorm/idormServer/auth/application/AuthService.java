package idorm.idormServer.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.AuthUseCase;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.application.port.in.dto.LoginRequest;
import idorm.idormServer.auth.application.port.out.EncryptPort;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements AuthUseCase {

    private final LoadMemberPort loadMemberPort;
    private final EncryptPort encryptorPort;

    @Override
    public AuthResponse login(final LoginRequest request) {
        Member member = loadMemberPort.loadMember(request.email(), encryptorPort.encrypt(request.password()));

        return new AuthResponse(member.getId(), member.getRoleType().getName(), member.getNickname().getValue());
    }
}
