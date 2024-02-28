package idorm.idormServer.photo.application.port.out;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.member.entity.Member;

public interface DeleteFilePort {

	void deleteMemberPhotoFile(String profileUrl);

	// TODO : 사진 일부 삭제, transaction 대상 아니므로 사진 Url들 받아오기
	void deletePostPhotoFiles(Post post);
}