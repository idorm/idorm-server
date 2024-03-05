package idorm.idormServer.common.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Builder;

public record ErrorResponse(String responseCode, String responseMessage, @JsonIgnore HttpStatus status) {

	@Builder
	public ErrorResponse {
	}

	public static ErrorResponse of(BaseResponseCode code) {
		return ErrorResponse.builder()
			.status(code.getStatus())
			.responseCode(code.getName())
			.responseMessage(code.getMessage())
			.build();
	}

	public static ErrorResponse of(HttpStatus status, String responseMessage, String responseCode) {
		return ErrorResponse.builder().responseCode(responseCode)
			.responseMessage(responseMessage).status(status).build();
	}
}