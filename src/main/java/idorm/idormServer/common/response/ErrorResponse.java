package idorm.idormServer.common.response;

import idorm.idormServer.common.exception.BaseResponseCode;
import idorm.idormServer.common.exception.GlobalResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class ErrorResponse extends BaseResponse {

  private final HttpStatus status;

  @Builder
  public ErrorResponse(String responseCode, String responseMessage, HttpStatus status) {
    super(false, responseCode, responseMessage);
    this.status = status;
  }

  public static ErrorResponse of(BaseResponseCode code) {
    return ErrorResponse.builder()
        .responseCode(code.getName())
        .responseMessage(code.getMessage())
        .status(code.getStatus())
        .build();
  }


  public static ErrorResponse of(BaseResponseCode code, String message) {
    return ErrorResponse.builder().responseCode(code.getName())
        .responseMessage(code.getMessage()).status(code.getStatus()).build();
  }

}
