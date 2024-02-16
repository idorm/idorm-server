package idorm.idormServer.member.application.port.in;

import idorm.idormServer.auth.domain.AuthInfo;

public interface MemberPhotoUseCase {

	void savePhoto(AuthInfo authInfo);

	void deletePhoto(AuthInfo authInfo);
}
