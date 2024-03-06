package idorm.idormServer.report.adapter.in.web;

import static idorm.idormServer.report.adapter.out.ReportResponseCode.*;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.auth.adapter.in.api.Auth;
import idorm.idormServer.auth.adapter.in.api.AuthInfo;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.common.response.SuccessResponse;
import idorm.idormServer.report.adapter.out.ReportResponseCode;
import idorm.idormServer.report.application.port.in.ReportUseCase;
import idorm.idormServer.report.application.port.in.dto.ReportRequest;
import idorm.idormServer.report.entity.ReportType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "X3. Report", description = "신고 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
public class ReportController {

	private final ReportUseCase reportUseCase;

	@Auth
	@Operation(summary = "회원/게시글/댓글/대댓글 신고")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "*_REPORTED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400",
			description = "FIELD_REQUIRED / MEMBERORPOSTORCOMMENTID_NEGATIVEORZERO_INVALID / " +
				"*_CHARACTER_INVALID"),
		@ApiResponse(responseCode = "401", description = "UNAUTHORIZED_PASSWORD"),
		@ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND / POST_NOT_FOUND / COMMENT_NOT_FOUND"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR")
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<SuccessResponse<Object>> report(
		@AuthInfo AuthResponse auth,
		@RequestBody @Valid ReportRequest request) {

		ReportResponseCode responseCode = MEMBER_REPORTED;

		if (ReportType.isMemberReport(request.reportType())) {
			reportUseCase.reportMember(auth, request);
		} else if (ReportType.isPostReport(request.reportType())) {
			reportUseCase.reportPost(auth, request);
			responseCode = POST_REPORTED;
		} else {
			reportUseCase.reportComment(auth, request);
			responseCode = COMMENT_REPORTED;
		}

		return ResponseEntity.status(201).body(SuccessResponse.from(responseCode));
	}
}