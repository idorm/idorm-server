package idorm.idormServer.community.post.application.port.in.dto;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.member.entity.Member;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostSaveRequest(
	@NotNull(message = "기숙사 카테고리는 공백일 수 없습니다.")
	String dormCategory,

	@NotBlank(message = "게시글 제목은 공백일 수 없습니다.")
	@Size(min = 1, max = 30, message = "title , 게시글 제목은 1~30자 이내여야 합니다.")
	String title,

	@NotBlank(message = "게시글 내용은 공백일 수 없습니다.")
	@Size(min = 1, max = 50, message = "content , 게시글 내용은 1~50자 이내여야 합니다.")
	String content,

	@NotNull(message = "익명 여부는 공백일 수 없습니다.")
	Boolean isAnonymous,

	List<MultipartFile> files
) {

	public Post toEntity(final Member member){
		return new Post(member, DormCategory.valueOf(dormCategory), title, content, isAnonymous);
	}

}
