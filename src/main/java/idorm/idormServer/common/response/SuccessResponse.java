package idorm.idormServer.common.response;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.GlobalResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SuccessResponse<T> extends BaseResponse {

	private final T data;
	private final HttpStatus httpStatus;

	@Builder
	public SuccessResponse(T data, String responseCode, String responseMessage,
		HttpStatus httpStatus) {
		super(true, responseCode, responseMessage);
		this.data = data;
		this.httpStatus = httpStatus;
	}

	public static <T> SuccessResponse<T> of(GlobalResponseCode globalResponseCode, T data) {
		return SuccessResponse.<T>builder()
			.httpStatus(globalResponseCode.getStatus())
			.responseCode(globalResponseCode.name())
			.data(data)
			.responseMessage(globalResponseCode.getMessage())
			.build();
	}

	public static <T> SuccessResponse<T> empty(GlobalResponseCode globalResponseCode) {
		return new SuccessResponse<>(null,
			globalResponseCode.name(), globalResponseCode.getMessage(), globalResponseCode.getStatus());
	}

}
