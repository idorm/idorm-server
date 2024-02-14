package idorm.idormServer.calendar.adapter.in.web;

import idorm.idormServer.calendar.application.port.in.OfficialCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.CrawledOfficialCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarUpdateRequest;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarsFindRequest;
import idorm.idormServer.common.dto.DefaultResponseDto;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.support.token.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "2. Official Calendar", description = "공식 일정 api")
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendarController {

    private final OfficialCalendarUseCase officialCalendarUseCase;

    @Operation(summary = "[관리자 용] 공식 일정 저장 및 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "CALENDAR_UPDATED",
                    content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "400", description = "ILLEGAL_ARGUMENT_DATE_SET"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
            @ApiResponse(responseCode = "404", description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @PostMapping("/admin/calendar")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> update(
            @Login Member member,
            @RequestBody @Valid OfficialCalendarUpdateRequest request
    ) {
        officialCalendarUseCase.update(member, request);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDAR_UPDATED")
                        .responseMessage("공식 일정 수정 완료")
                        .build());
    }

    @Operation(summary = "[관리자 용] 일정 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "OFFICIAL_CALENDAR_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400", description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
            @ApiResponse(responseCode = "404", description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @DeleteMapping("/admin/calendar/{official-calendar-id}")
    public ResponseEntity<DefaultResponseDto<Object>> delete(
            @Login Member member,
            @PathVariable(value = "official-calendar-id")
            @Positive(message = "삭제할 공식 일정 식별자는 양수만 가능합니다.") Long officialCalendarId
    ) {

        officialCalendarUseCase.delete(member, officialCalendarId);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDAR_DELETED")
                        .responseMessage("공식 일정 삭제 완료")
                        .build());
    }

    @Operation(summary = "[관리자 용] 공식 일정 전체 조회", description = "- 리스트에 일정이 담겨서 응답합니다.\n" +
            "- 일정 조회 조건은 다음과 같습니다.\n" +
            "\t- 생활원 게시글 작성일자 : <b>이번 달</b> 혹은 <b>일주일 전의 달</b> 인 경우\n" +
            "- 응답 필드의 <b>isPublic(사용자 공개 여부, 관리자 허가 여부)</b> 값으로 프론트에서 추가 가공이 필요합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "OFFICIAL_CALENDARS_FOUND",
                    content = @Content(schema = @Schema(implementation = CrawledOfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "400", description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
            @ApiResponse(responseCode = "404", description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/admin/calendars")
    public ResponseEntity<DefaultResponseDto<Object>> findManyByAdmin(@Login Member member) {

        officialCalendarUseCase.findAllByAdmin(member);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDARS_FOUND")
                        .responseMessage("관리자용 월별 공식 일정 조회 완료")
                        .build());
    }

    @Operation(summary = "[관리자 용] 공식 일정 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "CALENDAR_FOUND",
                    content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "400", description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "404", description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/admin/calendar/{official-calendar-id}")
    public ResponseEntity<DefaultResponseDto<Object>> findOne(
            @Login Member member,
            @PathVariable(value = "official-calendar-id")
            @Positive(message = "삭제할 일정 식별자는 양수만 가능합니다.") Long officialCalendarId
    ) {

        officialCalendarUseCase.findOneByAdmin(member, officialCalendarId);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDAR_FOUND")
                        .responseMessage("관리자용 공식 일정 단건 조회 완료")
                        .build()
                );
    }

    @Operation(summary = "[사용자 용] 공식 일정 월별 조회", description = "- 모든 기숙사의 일정을 반환합니다. \n" +
            "- 종료된 일정은 제거한 후, 최신 등록 순으로 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "ILLEGAL_ARGUMENT_DATE_SET"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "ACCESS_DENIED"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/member/calendars")
    public ResponseEntity<DefaultResponseDto<Object>> findManyByMember(
            @Login Member member,
            @RequestBody @Valid OfficialCalendarsFindRequest request
    ) {

        officialCalendarUseCase.findAllByMember(member, request);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDARS_FOUND")
                        .responseMessage("월별 공식 일정 다건 조회")
                        .build()
                );
    }
}
