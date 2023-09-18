package idorm.idormServer.master.develop;

import com.google.firebase.messaging.Message;
import idorm.idormServer.application.OfficialCalendarCrawler;
import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.fcm.dto.FcmRequest;
import idorm.idormServer.fcm.dto.NotifyType;
import idorm.idormServer.fcm.service.FCMService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Tag(name = "Develop", description = "개발용 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class DevelopController {

    private final JwtTokenProvider jwtTokenProvider;
    private final FCMService fcmService;
    private final MemberService memberService;
    private final DevelopService testService;
    private final OfficialCalendarCrawler officialCalendarCrawler;

    @Hidden
    @Operation(summary = "공식 일정 크롤링")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CRAWLING_COMPLETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "500", description = "CRAWLING_SERVER_ERROR")
    })
    @GetMapping("/crawling")
    public ResponseEntity<DefaultResponseDto<Object>> crawlingOfficialCalendar() {

        officialCalendarCrawler.crawling();

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CRAWLING_COMPLETED")
                        .responseMessage("공식 일정 크롤링 완료")
                        .build());
    }

    @Hidden
    @Operation(summary = "푸시 알람 전송", description = "- 실제로는 서버에서 전송합니다. \n" +
            "- 실 운영 시 삭제 예정\n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TOKEN_RENEWED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND")
    })
    @PostMapping("/fcm")
    public ResponseEntity pushMessage(
//            @RequestBody TestRequestDto request
    ) {

        Member member2 = memberService.findById(2L);

        List<FcmRequest> fcmMessages = new ArrayList<>();
        for (int i = 2; i < 7; i++) {
            if (i == 6) {
                FcmRequest topPostFcmMessage = FcmRequest.builder()
                        .token(member2.getFcmToken())
                        .notification(FcmRequest.Notification.builder()
                                .notifyType(NotifyType.TOPPOST)
                                .contentId(1L)
                                .title("어제 " + 1 + " 기숙사의 인기 게시글 입니다.")
                                .content("test")
                                .build())
                        .build();
                fcmMessages.add(topPostFcmMessage);
            } else {
                FcmRequest topPostFcmMessage = FcmRequest.builder()
                        .token("sfsdfa")
                        .notification(FcmRequest.Notification.builder()
                                .notifyType(NotifyType.TOPPOST)
                                .contentId(1L)
                                .title("어제 " + 1 + " 기숙사의 인기 게시글 입니다.")
                                .content("test")
                                .build())
                        .build();
                fcmMessages.add(topPostFcmMessage);
            }
        }

        List<Message> messages = fcmService.createMessageOfTeamCalendar(fcmMessages);
        fcmService.sendManyMessages(messages);

        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "계정 토큰 발급", description = "- 실 운영 시 삭제 예정\n" +
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

    @Hidden
    @Operation(summary = "데이터베이스 초기화", description = "- DB 초기화 후 테스트용 데이터를 다시 주입합니다.\n" +
            "- 400개의 데이터를 재주입하므로 약 10초 소요됩니다.")
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
}
