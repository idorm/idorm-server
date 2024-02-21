package idorm.idormServer.common.response;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse {

	private final String responseCode;
	private final String responseMessage;
	private final HttpStatus status;

	@Builder
	public ErrorResponse(String responseCode, String responseMessage, HttpStatus status) {
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
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