package idorm.idormServer.community.postPhoto.adapter.out;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostPhotoResponseCode implements BaseResponseCode {

    //실패
    NOT_FOUND_POSTPHOTO(NOT_FOUND, "등록된 게시글 사진이 없습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}