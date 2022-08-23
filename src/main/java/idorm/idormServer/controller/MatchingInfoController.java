package idorm.idormServer.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.domain.MatchingInfo;
import idorm.idormServer.domain.Member;
import idorm.idormServer.dto.MatchingInfoDTO;
import idorm.idormServer.dto.MemberDTO;
import idorm.idormServer.jwt.JwtTokenProvider;
import idorm.idormServer.service.MatchingInfoService;
import idorm.idormServer.service.MemberService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.time.LocalDateTime;

import static idorm.idormServer.dto.MatchingInfoDTO.*;
import static idorm.idormServer.dto.MemberDTO.*;

@Slf4j
@Tag(name = "matchingInfo", description = "온보딩 정보 API")
@RestController
@RequiredArgsConstructor
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
            @ApiResponse(code = 200, message = "온보딩 정보 저장 성공")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dormNum", value = "기숙사1,기숙사2,기숙사3", required = true),
            @ApiImplicitParam(name = "joinPeriod", value = "WEEK16,WEEK24", required = true),
            @ApiImplicitParam(name = "gender", value = "MALE, FEMALE", required = true),
            @ApiImplicitParam(name = "age", value = "나이", required = true),
            @ApiImplicitParam(name = "isSnoring", value = "코골이 여부", required = true),
            @ApiImplicitParam(name = "isGrinding", value = "이갈이 여부", required = true),
            @ApiImplicitParam(name = "isSmoking", value = "흡연 여부", required = true),
            @ApiImplicitParam(name = "isAllowedFood", value = "실내 음식 섭취 허용 여부", required = true),
            @ApiImplicitParam(name = "isWearEarphones", value = "이어폰 착용 여부", required = true),
            @ApiImplicitParam(name = "wakeUpTime", value = "기상 시간", required = true),
            @ApiImplicitParam(name = "cleanUpStatus", value = "청소 상태", required = true),
            @ApiImplicitParam(name = "showerTime", value = "샤워 시간", required = true),
            @ApiImplicitParam(name = "openKakaoLink", value = "오픈채팅 링크", required = false),
            @ApiImplicitParam(name = "mbti", value = "mbti", required = false),
            @ApiImplicitParam(name = "wishText", value = "하고싶은 말", required = false)
    })
    public ResponseEntity<DefaultResponseDto<Object>> saveMatchingInfo(@RequestBody @Valid MatchingInfoSaveRequestDTO request, HttpServletRequest request2) throws Exception {
        log.info("START | MatchingInfo Controller 저장 At " + LocalDateTime.now());
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(userPk);

//        if(member == null) {
//            return ResponseEntity.status(400)
//                    .body(DefaultResponseDto.builder()
//                    .responseCode("REQUIRED_USER_LOGIN")
//                    .responseMessage("로그인이 필요합니다.")
//                    .build());
//        }

        if(member.getMatchingInfo() == null) { // 등록된 매칭정보가 없다면

            Long matchingInfoId = matchingInfoService.save(request);
            matchingInfoService.updateMatchingInfoAddMember(matchingInfoId, member);

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("온보딩 정보 저장 완료")
                            .data(matchingInfoId)
                            .build()
                    );
        } else {
            throw new IllegalArgumentException("이미 등록된 매칭정보가 있습니다.");
        }
    }

}
