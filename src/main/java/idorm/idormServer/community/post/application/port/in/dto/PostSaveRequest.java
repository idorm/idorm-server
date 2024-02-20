package idorm.idormServer.community.post.application.port.in.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostSaveRequest(
	@NotNull(message = "기숙사 카테고리는 공백일 수 없습니다.")
	String dormCategory,

	@NotBlank(message = "게시글 제목은 공백일 수 없습니다.")
	String title,

	@NotBlank(message = "게시글 내용은 공백일 수 없습니다.")
	String content,

	@Schema(required = true, description = "익명 여부")
	Boolean isAnonymous,

	@Schema(description = "게시글 사진들")
	List<MultipartFile> files
) {

}
