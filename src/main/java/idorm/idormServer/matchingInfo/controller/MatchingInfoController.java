package idorm.idormServer.matchingInfo.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exception.CustomException;

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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static idorm.idormServer.exception.ExceptionCode.*;

@Api(tags = "온보딩 매칭 정보")
@Validated
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingInfoController {

    private final MatchingInfoService matchingInfoService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "온보딩 정보 생성", notes = "- 최초로 온보딩 정보를 저장할 경우만 사용 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "MATCHINGINFO_SAVED",
                    content = @Content(schema = @Schema(implementation = MatchingInfoDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_MATCHINGINFO"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/member/matchinginfo")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> save(
            HttpServletRequest servletRequest,
            @RequestBody @Valid MatchingInfoDefaultRequestDto request) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(memberId);

        if(member.getMatchingInfo() != null) {
            throw new CustomException(null, DUPLICATE_MATCHINGINFO);
        }

        MatchingInfo matchingInfo = matchingInfoService.save(request.toEntity(member));

        MatchingInfoDefaultResponseDto response = new MatchingInfoDefaultResponseDto(matchingInfo);

        return ResponseEntity.status(201)
            .body(DefaultResponseDto.builder()
                    .responseCode("MATCHINGINFO_SAVED")
                    .responseMessage("MatchingInfo 저장 완료")
                    .data(response)
                    .build()
            );
    }

    @PutMapping("/member/matchinginfo")
    @ApiOperation(value = "온보딩 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "MATCHINGINFO_UPDATED",
                    content = @Content(schema = @Schema(implementation = MatchingInfoDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "MATCHINGINFO_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> updateMatchingInfo(
            HttpServletRequest servletRequest,
            @RequestBody @Valid MatchingInfoDefaultRequestDto request) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() == null) {
            throw new CustomException(null, MATCHINGINFO_NOT_FOUND);
        }

        matchingInfoService.updateMatchingInfo(member.getMatchingInfo(), request);
        MatchingInfoDefaultResponseDto response = new MatchingInfoDefaultResponseDto(member.getMatchingInfo());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MATCHINGINFO_UPDATED")
                        .responseMessage("MatchingInfo 수정 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "온보딩 정보 공개 여부 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "ISMATCHINGINFOPUBLIC_UPDATED"),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "MATCHINGINFO_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PatchMapping("/member/matchinginfo")
    public ResponseEntity<DefaultResponseDto<Object>> updateisMatchingInfoPublic(
            HttpServletRequest servletRequest,
            MatchingInfoUpdateIsPublicRequestDto request) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(memberId);

        if(member.getMatchingInfo() == null) {
            throw new CustomException(null, MATCHINGINFO_NOT_FOUND);
        }

        matchingInfoService.updateMatchingInfoIsPublic(member.getMatchingInfo(), request.getIsMatchingInfoPublic());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("ISMATCHINGINFOPUBLIC_UPDATED")
                        .responseMessage("MatchingInfo 공개여부 수정 완료")
                        .build()
                );
    }

    @GetMapping("/member/matchinginfo")
    @ApiOperation(value = "온보딩 정보 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "MATCHINGINFO_FOUND",
                    content = @Content(schema = @Schema(implementation = MatchingInfoDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "MATCHINGINFO_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findMatchingInfo(
            HttpServletRequest servletRequest) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(memberId);

        if(member.getMatchingInfo() == null) { // 등록된 매칭정보가 없다면
            throw new CustomException(null, MATCHINGINFO_NOT_FOUND);
        }

        MatchingInfo foundMatchingInfo = matchingInfoService.findById(member.getMatchingInfo().getId());

        MatchingInfoDefaultResponseDto response = new MatchingInfoDefaultResponseDto(foundMatchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MATCHINGINFO_FOUND")
                        .responseMessage("MatchingInfo 단건 조회")
                        .data(response)
                        .build()
                );
    }

    @DeleteMapping("/member/matchinginfo")
    @ApiOperation(value = "온보딩 정보 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "MATCHINGINFO_DELETED"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "MATCHINGINFO_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> deleteMatchingInfo(HttpServletRequest request2) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() == null) { // 등록된 매칭정보가 없는 경우
            throw new CustomException(null, MATCHINGINFO_NOT_FOUND);
        }

        matchingInfoService.delete(member.getMatchingInfo());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MATCHINGINFO_DELETED")
                        .responseMessage("MatchingInfo 삭제 완료")
                        .build()
                );
    }
}
