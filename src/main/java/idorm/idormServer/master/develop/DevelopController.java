package idorm.idormServer.master.develop;

import idorm.idormServer.application.OfficialCalendarCrawler;
import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.calendar.dto.CrawledOfficialCalendarResponse;
import idorm.idormServer.common.DefaultResponseDto;
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

@Tag(name = "1. Develop", description = "개발용 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class DevelopController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final DevelopService testService;
    private final OfficialCalendarCrawler crawler;

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

    @Operation(summary = "생활원 홈페이지 크롤링")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @GetMapping("/crawling")
    public ResponseEntity<DefaultResponseDto<Object>> crawling() {

        List<CrawledOfficialCalendarResponse> crawledOfficialCalendarResponses = crawler.crawlPosts();

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CRAWLING_COMPLETED")
                        .responseMessage("크롤링 완료")
                        .data(crawledOfficialCalendarResponses)
                        .build());
    }
}
