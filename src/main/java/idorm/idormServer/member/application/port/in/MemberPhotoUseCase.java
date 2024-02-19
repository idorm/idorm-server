package idorm.idormServer.member.application.port.in;

import org.springframework.web.multipart.MultipartFile;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;

public interface MemberPhotoUseCase {

	void savePhoto(AuthResponse auth, MultipartFile file);

	void deletePhoto(AuthResponse auth);
}
