package idorm.idormServer.common.dto;

import idorm.idormServer.common.exception.ExceptionCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@Schema(title = "기본 에러 응답")
public class DefaultExceptionResponseDto {

    @Schema(description = "에러 코드", example = "EXCEPTION_CODE")
    private String responseCode;

    @Schema(format = "String", description = "에러 메세지", example = "에러 메세지")
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
