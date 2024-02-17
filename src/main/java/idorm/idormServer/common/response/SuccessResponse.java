package idorm.idormServer.common.response;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@ToString
public class SuccessResponse<T> {

  private final T data;
  private final String responseCode;
  private final String responseMessage;

  public SuccessResponse(BaseResponseCode baseResponseCode) {
    this.responseCode = baseResponseCode.getName();
    this.responseMessage = baseResponseCode.getMessage();
    this.data = null;
  }


  public static <T> SuccessResponse<T> of(BaseResponseCode baseResponseCode, T data) {
    return SuccessResponse.<T>builder()
        .responseCode(baseResponseCode.getName())
        .responseMessage(baseResponseCode.getMessage())
        .data(data)
        .build();
  }

  public static SuccessResponse empty(BaseResponseCode baseResponseCode) {
    return SuccessResponse.builder()
        .responseCode(baseResponseCode.getName())
        .responseMessage(baseResponseCode.getMessage())
        .data(null)
        .build();
  }

}
