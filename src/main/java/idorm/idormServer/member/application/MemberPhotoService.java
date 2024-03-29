package idorm.idormServer.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.member.application.port.in.MemberPhotoUseCase;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.entity.Member;
import idorm.idormServer.photo.application.port.out.DeleteFilePort;
import idorm.idormServer.photo.application.port.out.SaveFilePort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberPhotoService implements MemberPhotoUseCase {

	private final LoadMemberPort loadMemberPort;
	private final SaveFilePort saveFilePort;
	private final DeleteFilePort deleteFilePort;

	@Override
	@Transactional
	public void savePhoto(final AuthResponse auth, final MultipartFile file) {
		Member member = loadMemberPort.loadMember(auth.getId());
		if (member.existsOfMemberPhoto()) {
			deleteFilePort.deleteMemberPhotoFile(member.getProfilePhotoUrl());
		}

		String photoUrl = saveFilePort.saveMemberPhotoFile(member, file);
		member.updateMemberPhoto(photoUrl);
	}

	@Override
	@Transactional
	public void deletePhoto(final AuthResponse auth) {
		Member member = loadMemberPort.loadMember(auth.getId());
		String photoUrl = member.getProfilePhotoUrl();

		member.deleteMemberPhoto();
		deleteFilePort.deleteMemberPhotoFile(photoUrl);
	}
}
