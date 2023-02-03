package idorm.idormServer.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@ApiModel(value = "기본 에러 응답")
public class DefaultExceptionResponseDto {

    @ApiModelProperty(position = 1, value = "에러 코드", example = "EXCEPTION_CODE")
    private String responseCode;

    @ApiModelProperty(position = 2, dataType = "String", value = "에러 메세지", example = "에러 메세지")
    private String responseMessage;

    public static ResponseEntity<DefaultExceptionResponseDto> exceptionResponse(final ExceptionCode exceptionCode) {
        return ResponseEntity
                .status(exceptionCode.getHttpStatus())
                .body(DefaultExceptionResponseDto.builder()
                        .responseCode(exceptionCode.name())
                        .responseMessage(exceptionCode.getMessage())
                        .build()
                );
    }
}
