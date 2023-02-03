package idorm.idormServer.matchingInfo.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.exception.DefaultExceptionResponseDto;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.dto.MatchingInfoDefaultResponseDto;
import idorm.idormServer.matchingInfo.dto.MatchingInfoDefaultRequestDto;
import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.matchingInfo.dto.MatchingInfoUpdateIsPublicRequestDto;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static idorm.idormServer.exception.ExceptionCode.*;

@Api(tags = "온보딩 매칭 정보")
@RestController
@RequiredArgsConstructor
public class MatchingInfoController {

    private final MatchingInfoService matchingInfoService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "MatchingInfo 저장", notes = "최초로 온보딩 정보를 저장할 경우만 사용 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MatchingInfoDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / DORM_CATEGORY_FORMAT_INVALID / JOIN_PERIOD_FORMAT_INVALID",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_MATCHING_INFO",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
    })
    @PostMapping("/member/matchinginfo")
    public ResponseEntity<DefaultResponseDto<Object>> saveMatchingInfo(
            HttpServletRequest request2, @RequestBody @Valid MatchingInfoDefaultRequestDto request) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() != null) { // 등록된 매칭정보가 있다면
            throw new CustomException(DUPLICATE_MATCHING_INFO);
        }

        MatchingInfo createdMatchingInfo = matchingInfoService.save(request, member);
        MatchingInfoDefaultResponseDto response = new MatchingInfoDefaultResponseDto(createdMatchingInfo);

        return ResponseEntity.status(200)
            .body(DefaultResponseDto.builder()
                    .responseCode("OK")
                    .responseMessage("MatchingInfo 저장 완료")
                    .data(response)
                    .build()
            );
    }

    @PutMapping("/member/matchinginfo")
    @ApiOperation(value = "MatchingInfo 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MatchingInfoDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / DORM_CATEGORY_FORMAT_INVALID / JOIN_PERIOD_FORMAT_INVALID",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "MATCHING_INFO_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
    })
    public ResponseEntity<DefaultResponseDto<Object>> updateMatchingInfo(
            HttpServletRequest request2,
            @RequestBody @Valid MatchingInfoDefaultRequestDto updateRequestDto) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() == null) { // 등록된 매칭정보가 없다면
            throw new CustomException(MATCHING_INFO_NOT_FOUND);
        }

        MatchingInfo updateMatchingInfo = member.getMatchingInfo();

        matchingInfoService.updateMatchingInfo(updateMatchingInfo.getId(), updateRequestDto);
        MatchingInfoDefaultResponseDto response = new MatchingInfoDefaultResponseDto(updateMatchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("MatchingInfo 수정 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "MatchingInfo 공개여부 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "MATCHING_INFO_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PatchMapping("/member/matchinginfo")
    public ResponseEntity<DefaultResponseDto<Object>> updateMatchingInfoIsPublic(
            HttpServletRequest request2,
            MatchingInfoUpdateIsPublicRequestDto requestDto) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() == null) { // 등록된 매칭정보가 없다면
            throw new CustomException(MATCHING_INFO_NOT_FOUND);
        }

        matchingInfoService.updateMatchingInfoIsPublic(
                member,
                requestDto.getIsMatchingInfoPublic());

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("NO_CONTENT")
                        .responseMessage("MatchingInfo 공개여부 수정 완료")
                        .build()
                );
    }

    @GetMapping("/member/matchinginfo")
    @ApiOperation(value = "MatchingInfo 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MatchingInfoDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "MATCHING_INFO_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findMatchingInfo(HttpServletRequest request2) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() == null) { // 등록된 매칭정보가 없다면
            throw new CustomException(MATCHING_INFO_NOT_FOUND);
        }

        Long matchingInfoId = member.getMatchingInfo().getId();
        MatchingInfo foundMatchingInfo = matchingInfoService.findById(matchingInfoId);

        MatchingInfoDefaultResponseDto response = new MatchingInfoDefaultResponseDto(foundMatchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("MatchingInfo 단건 조회")
                        .data(response)
                        .build()
                );
    }

    @DeleteMapping("/member/matchinginfo")
    @ApiOperation(value = "MatchingInfo 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "MATCHING_INFO_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<DefaultResponseDto<Object>> deleteMatchingInfo(HttpServletRequest request2) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() == null) { // 등록된 매칭정보가 없는 경우
            throw new CustomException(MATCHING_INFO_NOT_FOUND);
        }

        MatchingInfo matchingInfo = member.getMatchingInfo();
        matchingInfoService.deleteMatchingInfo(matchingInfo);

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("NO_CONTENT")
                        .responseMessage("MatchingInfo 삭제 완료")
                        .build()
                );
    }
}
