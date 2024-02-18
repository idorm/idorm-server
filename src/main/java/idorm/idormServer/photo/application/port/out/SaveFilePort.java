package idorm.idormServer.photo.application.port.out;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.member.domain.Member;

public interface SaveFilePort {

	String saveMemberPhotoFile(Member member, MultipartFile multipartFile);

	List<String> savePostPhotoFiles(Post post, List<MultipartFile> multipartFiles);
}