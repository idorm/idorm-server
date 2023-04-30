package idorm.idormServer.development.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.calendar.dto.CalendarDefaultResponseDto;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.development.dto.TestRequestDto;
import idorm.idormServer.development.service.TestService;
import idorm.idormServer.fcm.dto.FcmRequestDto;
import idorm.idormServer.fcm.service.FCMService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Api(tags = "개발용")
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final JwtTokenProvider jwtTokenProvider;
    private final FCMService firebaseCloudMessageService;
    private final MemberService memberService;
    private final TestService testService;

    @ApiOperation(value = "[테스트용] 푸시 알람 전송", notes = "- 실제로는 서버에서 전송합니다. \n" +
            "- 실 운영 시 삭제 예정\n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TOKEN_RENEWED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND")
    })
    @PostMapping("/fcm")
    public ResponseEntity pushMessage(
            @RequestBody TestRequestDto request) {

        Member member = memberService.findById(request.getMemberId());

        FcmRequestDto fcmRequest = FcmRequestDto.builder()
                .token(request.getTargetToken())
                .notification(FcmRequestDto.Notification.builder()
                        .notifyType(request.getAlertType())
                        .contentId(request.getContentId())
                        .title(request.getTitle())
                        .content(request.getBody())
                        .build())
                .build();

        firebaseCloudMessageService.sendMessage(member, fcmRequest);

        return ResponseEntity.status(204).build();
    }

    @ApiOperation(value = "[개발용] 계정 토큰 발급", notes = "- 실 운영 시 삭제 예정\n" +
            "- 회원 식별자를 주면 access token을 헤더로 반환합니다.\n" +
            "- 2번 식별자부터 사용 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TOKEN_RENEWED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND")
    })
    @GetMapping("/token/{test-id}")
    public ResponseEntity<DefaultResponseDto<Object>> renewToken(@PathVariable(value = "test-id") Long testId) {

        Member member = memberService.findById(testId);

        Iterator<String> iter = member.getRoles().iterator();
        List<String> roles = new ArrayList<>();

        while (iter.hasNext()) {
            roles.add(iter.next());
        }

        String newTokens = jwtTokenProvider.createToken(String.valueOf(member.getId()), roles);

        return ResponseEntity.status(200)
                .header(AUTHORIZATION, newTokens)
                .body(DefaultResponseDto.builder()
                        .responseCode("TOKEN_RENEWED")
                        .responseMessage("테스트용 계정 토큰 재발급 완료")
                        .build());
    }

    @ApiOperation(value = "데이터베이스 초기화", notes = "- DB 초기화 후 테스트용 데이터를 다시 주입합니다.\n" +
            "- 400개의 데이터를 재주입하므로 30초 이상의 시간이 소요됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "DATABASE_RESET",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping
    public ResponseEntity<DefaultResponseDto<Object>> resetDatabase() {

        testService.resetDatabase();

        return ResponseEntity.status(204).build();
    }

    @ApiOperation(value = "[테스트용] 3 기숙사 오전 푸시알림 기능 테스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "OK",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/fcm/scheduled")
    public ResponseEntity<DefaultResponseDto<Object>> sendScheduledFcmMessage() {

        testService.alertDorm3();

        return ResponseEntity.status(204).build();
    }

    @ApiOperation(value = "[테스트용] 3 기숙사 오늘의 일정 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DORM3_TODAY_CALENDAR_FOUND",
                    content = @Content(schema = @Schema(implementation = CalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @PostMapping("/fcm/calendar")
    public ResponseEntity<DefaultResponseDto<Object>> findTodayCalendarFromDorm3() {

        List<CalendarDefaultResponseDto> responses = testService.findTodayCalendarOfDorm3();

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("DORM3_TODAY_CALENDAR_FOUND")
                        .responseMessage("3 기숙사 오늘의 일정 조회 완료")
                        .data(responses)
                        .build());
    }
}
