package idorm.idormServer.community.post.application.port.in.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record PostUpdateRequest(

    @NotBlank(message = "게시글 제목은 공백일 수 없습니다.")
    @Size(min = 1, max = 30, message = "title , 게시글 제목은 1~30자 이내여야 합니다.")
    String title,

    @NotBlank(message = "게시글 내용은 공백일 수 없습니다.")
    @Size(min = 1, max = 50, message = "content , 게시글 내용은 1~50자 이내여야 합니다.")
    String content,

    @NotNull(message = "익명 여부는 공백일 수 없습니다.")
    Boolean isAnonymous,

    List<Long> deletePostPhotoIds,

    List<MultipartFile> files
) {

}
