package idorm.idormServer.member.application.port.in;

import idorm.idormServer.member.domain.Member;
import javax.servlet.http.HttpServletRequest;

public interface JwtTokenUseCase {

    String reissueAccessToken(HttpServletRequest servletRequest, Member member);

    void deleteRefreshToken(Long memberId);
}
