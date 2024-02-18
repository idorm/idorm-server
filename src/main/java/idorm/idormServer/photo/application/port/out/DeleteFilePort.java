package idorm.idormServer.photo.application.port.out;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.member.domain.Member;

public interface DeleteFilePort {

	void deleteMemberPhotoFile(Member member);

	void deletePostPhotoFiles(Post post);
}