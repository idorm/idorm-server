package idorm.idormServer.fcm.controller;

import idorm.idormServer.auth.dto.AuthInfo;
import idorm.idormServer.common.dto.DefaultResponseDto;

import idorm.idormServer.support.token.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member FCM", description = "FCM api")
@Validated
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberFCMController {

    private final idorm.idormServer.fcm.service.MemberFCMService memberFCMService;

    @Operation(summary = "회원 FCM 토큰 등록")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "MEMBER_FCM_UPDATED"),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / EMAIL_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_PASSWORD"),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND / MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @PostMapping("/fcm")
    public ResponseEntity<DefaultResponseDto<Object>> saveFCM(
            @Login AuthInfo authInfo, @RequestBody @Valid FcmRequest request
    ) {


        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_FCM_UPDATED")
                        .responseMessage("회원 FCM 업데이트 완료")
                        .build());
    }
}
