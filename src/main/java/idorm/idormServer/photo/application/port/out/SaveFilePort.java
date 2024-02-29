package idorm.idormServer.photo.application.port.out;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.member.entity.Member;

public interface SaveFilePort {

	String saveMemberPhotoFile(Member member, MultipartFile multipartFile);

	List<String> addPostPhotoFiles(Post post, List<MultipartFile> multipartFiles);
}