package idorm.idormServer.exceptions;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@ApiModel(value = "에러 응답")
public class ErrorResponse {

    @ApiModelProperty(position = 1, value = "상위 에러명")
    private final String error;

    @ApiModelProperty(position = 2, value = "하위 에러명")
    private final String code;

    @ApiModelProperty(position = 3, value = "에러 메시지")
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .message(errorCode.getDetail())
                        .build()
        );
    }
}
