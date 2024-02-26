package idorm.idormServer.photo.application.port.out;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.member.entity.Member;

public interface DeleteFilePort {

	void deleteMemberPhotoFile(Member member);

	// TODO : 사진 일부 삭제
	void deletePostPhotoFiles(Post post);
}