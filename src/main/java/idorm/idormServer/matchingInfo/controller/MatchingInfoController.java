package idorm.idormServer.matchingInfo.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exceptions.http.ConflictException;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.dto.MatchingInfoDefaultResponseDto;
import idorm.idormServer.matchingInfo.dto.MatchingInfoDefaultRequestDto;
import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Api(tags = "MatchingInfo(온보딩) API")
public class MatchingInfoController {

    private final MatchingInfoService matchingInfoService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/member/matchinginfo")
    @ApiOperation(value = "MatchingInfo 저장", notes = "최초로 온보딩 정보를 저장할 경우만 사용 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "MatchingInfo 저장 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 404, message = "멤버를 찾을 수 없습니다."),
            @ApiResponse(code = 409, message = "이미 등록된 매칭정보가 있습니다."),
            @ApiResponse(code = 500, message = "MatchingInfo save 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> saveMatchingInfo(
            HttpServletRequest request2, @RequestBody @Valid MatchingInfoDefaultRequestDto request) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() != null) // 등록된 매칭정보가 있다면
            throw new ConflictException("이미 등록된 매칭정보가 있습니다.");

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
            @ApiResponse(code = 200, message = "MatchingInfo 수정 완료"),
            @ApiResponse(code = 400, message = "등록된 매칭정보가 없습니다."),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "MatchingInfo 수정 중 서버 에러 발생")
    })
    public ResponseEntity<DefaultResponseDto<Object>> updateMatchingInfo(HttpServletRequest request2, @RequestBody @Valid MatchingInfoDefaultRequestDto updateRequestDto) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() == null) // 등록된 매칭정보가 없다면
            throw new ConflictException("등록된 매칭정보가 없습니다.");

        MatchingInfo updateMatchingInfo = updateRequestDto.toEntity(member);

        matchingInfoService.updateMatchingInfo(member.getMatchingInfo().getId(), updateRequestDto);

        MatchingInfoDefaultResponseDto response = new MatchingInfoDefaultResponseDto(updateMatchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("MatchingInfo 수정 완료")
                        .data(response)
                        .build()
                );
    }

    @PatchMapping("/member/matchinginfoispublic")
    @ApiOperation(value = "MatchingInfo 공개여부 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "MatchingInfo 공개여부 수정 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "등록된 매칭정보가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "MatchingInfo 매칭이미지 공개 여부 변경 중 서버 에러 발생")
    })
    public ResponseEntity<DefaultResponseDto<Object>> updateMatchingInfoIsPublic(HttpServletRequest request2) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() == null) // 등록된 매칭정보가 없다면
            throw new ConflictException("등록된 매칭정보가 없습니다.");

        MatchingInfo updatedMatchingInfo = matchingInfoService.updateMatchingInfoIsPublic(member);

        MatchingInfoDefaultResponseDto response = new MatchingInfoDefaultResponseDto(updatedMatchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("MatchingInfo 공개여부 수정 완료")
                        .data(response)
                        .build()
                );
    }

    @GetMapping("/member/matchinginfo")
    @ApiOperation(value = "MatchingInfo 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "MatchingInfo 단건 조회"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "등록된 매칭정보가 없습니다."),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findMatchingInfo(HttpServletRequest request2) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() == null) // 등록된 매칭정보가 없다면
            throw new ConflictException("등록된 매칭정보가 없습니다.");

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
            @ApiResponse(code = 200, message = "MatchingInfo 삭제 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 404, message = "삭제할 매칭정보가 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "등록된 매칭정보가 없습니다."),
            @ApiResponse(code = 500, message = "MatchingInfo 삭제 중 서버 에러 발생")
    })
    public ResponseEntity<DefaultResponseDto<Object>> deleteMatchingInfo(HttpServletRequest request2) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        if(member.getMatchingInfo() == null) // 등록된 매칭정보가 없는 경우
            throw new ConflictException("등록된 매칭정보가 없습니다.");

        Long matchingInfoId = member.getMatchingInfo().getId();
        matchingInfoService.deleteMatchingInfo(matchingInfoId);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("MatchingInfo 삭제 완료")
                        .build()
                );
    }
}
