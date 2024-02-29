package idorm.idormServer.community.post.application.port.in.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

public record PostUpdateRequest(

	@NotBlank(message = "게시글 제목은 공백일 수 없습니다.")
	String title,

	@NotBlank(message = "게시글 내용용 공백일 수 없습니다.")
	String content,

	@NotNull(message = "익명 여부는 공백일 수 없습니다.")
	Boolean isAnonymous,

//	@NotNull(message = "익명 여부는 공백일 수 없습니다.")
	List<Long> deletePostPhotoIds,

	List<MultipartFile> files
) {
}
