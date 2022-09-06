package idorm.idormServer.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.domain.MatchingInfo;
import idorm.idormServer.domain.Member;
import idorm.idormServer.dto.MatchingInfo.MatchingInfoDefaultResponseDto;
import idorm.idormServer.dto.MatchingInfo.MatchingInfoSaveRequestDto;
import idorm.idormServer.dto.MatchingInfo.MatchingInfoUpdateRequestDto;
import idorm.idormServer.jwt.JwtTokenProvider;
import idorm.idormServer.service.MatchingInfoService;
import idorm.idormServer.service.MemberService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Api(tags = "온보딩 정보 API")
public class MatchingInfoController {

    private final MatchingInfoService matchingInfoService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 온보딩(매칭)정보 저장
     */
    @PostMapping("/matchinginfo")
    @ApiOperation(value = "온보딩 정보 저장", notes = "최초로 온보딩 정보를 저장할 경우만 사용 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "온보딩 정보 저장 성공"),
            @ApiResponse(code = 400, message = "이미 등록된 매칭정보가 있습니다."),
            @ApiResponse(code = 400, message = "JWT String argument cannot be null or empty (로그인 안 되어있을 경우)")
    })
    public ResponseEntity<DefaultResponseDto<Object>> saveMatchingInfo(HttpServletRequest request2, @RequestBody @Valid MatchingInfoSaveRequestDto request) {

        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(userPk);

        if(member.getMatchingInfo() != null) // 등록된 매칭정보가 있다면
            throw new IllegalArgumentException("이미 등록된 매칭정보가 있습니다.");

        Long matchingInfoId = matchingInfoService.save(request, member);

        MatchingInfo matchingInfo = matchingInfoService.findById(matchingInfoId);
        MatchingInfoDefaultResponseDto response = new MatchingInfoDefaultResponseDto(matchingInfo);

        return ResponseEntity.status(200)
            .body(DefaultResponseDto.builder()
                    .responseCode("OK")
                    .responseMessage("온보딩 정보 저장 완료")
                    .data(response)
                    .build()
            );
    }

    /**
     * 온보딩(매칭) 정보 수정
     */
    @PatchMapping("/matchinginfo")
    @ApiOperation(value = "온보딩 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "온보딩 정보 수정 성공"),
            @ApiResponse(code = 400, message = "등록된 매칭정보가 없습니다."),
            @ApiResponse(code = 400, message = "JWT String argument cannot be null or empty (로그인 안 되어있을 경우)")
    })
    public ResponseEntity<DefaultResponseDto<Object>> updateMatchingInfo(HttpServletRequest request2, @RequestBody @Valid MatchingInfoUpdateRequestDto request) {

        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(userPk);

        if(member.getMatchingInfo() == null) // 등록된 매칭정보가 없다면
            throw new IllegalArgumentException("등록된 매칭정보가 없습니다.");

        MatchingInfo matchingInfo = member.getMatchingInfo();

        matchingInfoService.updateMatchingInfo(matchingInfo, request);

        MatchingInfoDefaultResponseDto response = new MatchingInfoDefaultResponseDto(matchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("온보딩 정보 수정 성공")
                        .data(response)
                        .build()
                );
    }

    /**
     * 온보딩(매칭) 정보 조회
     */
    @GetMapping("/matchinginfo")
    @ApiOperation(value = "온보딩 정보 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "온보딩 정보 단건 조회 성공"),
            @ApiResponse(code = 400, message = "등록된 매칭정보가 없습니다."),
            @ApiResponse(code = 400, message = "JWT String argument cannot be null or empty (로그인 안 되어있을 경우)")
    })
    public ResponseEntity<DefaultResponseDto<Object>> findMatchingInfo(HttpServletRequest request2) {

        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(userPk);

        if(member.getMatchingInfo() == null) // 등록된 매칭정보가 없다면
            throw new IllegalArgumentException("등록된 매칭정보가 없습니다.");

        MatchingInfo matchingInfo = member.getMatchingInfo();

        MatchingInfoDefaultResponseDto response = new MatchingInfoDefaultResponseDto(matchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("온보딩 정보 단건 조회 성공")
                        .data(response)
                        .build()
                );
    }

    /**
     * 온보딩(매칭) 정보 삭제
     */
    @DeleteMapping("/matchinginfo")
    @ApiOperation(value = "온보딩 정보 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "온보딩 정보 삭제 성공"),
            @ApiResponse(code = 400, message = "등록된 매칭정보가 없습니다."),
            @ApiResponse(code = 400, message = "JWT String argument cannot be null or empty (로그인 안 되어있을 경우)")
    })
    public ResponseEntity<DefaultResponseDto<Object>> deleteMatchingInfo(HttpServletRequest request2) {

        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(userPk);

        if(member.getMatchingInfo() == null) // 등록된 매칭정보가 없는 경우
            throw new IllegalArgumentException("등록된 매칭정보가 없습니다.");


        Optional<MatchingInfo> matchingInfo = matchingInfoService.findByMemberIdOp(member.getId());
        matchingInfoService.deleteMatchingInfo(matchingInfo.get());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("온보딩 정보 삭제 성공")
                        .data(member.getEmail())
                        .build()
                );
    }


}
