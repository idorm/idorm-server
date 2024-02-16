package idorm.idormServer.member.application.port.in;

import idorm.idormServer.member.domain.Member;

public interface MemberPhotoUseCase {

    void savePhoto(Member member);

    void deletePhoto(Member member);
}
