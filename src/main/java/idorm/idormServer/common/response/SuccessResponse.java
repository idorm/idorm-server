package idorm.idormServer.common.response;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;

@Getter
public class SuccessResponse<T> {

	private final String responseCode;
	private final String responseMessage;
	private final T data;

	private SuccessResponse(BaseResponseCode baseResponseCode) {
		this.responseCode = baseResponseCode.getName();
		this.responseMessage = baseResponseCode.getMessage();
		this.data = null;
	}

	private SuccessResponse(BaseResponseCode baseResponseCode, T data) {
		this.responseCode = baseResponseCode.getName();
		this.responseMessage = baseResponseCode.getMessage();
		this.data = data;
	}

	public static <T> SuccessResponse<T> of(BaseResponseCode baseResponseCode, T data) {
		return new SuccessResponse<>(baseResponseCode, data);
	}

	public static <T> SuccessResponse<T> from(BaseResponseCode baseResponseCode) {
		return new SuccessResponse<>(baseResponseCode);
	}
}