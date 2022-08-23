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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.time.LocalDateTime;

import static idorm.idormServer.dto.MatchingInfoDTO.*;
import static idorm.idormServer.dto.MemberDTO.*;

@Slf4j
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
    public ResponseEntity<DefaultResponseDto<Object>> saveMatchingInfo(HttpServletRequest request2, @RequestBody @Valid CreateMatchingInfoRequest request) throws ResponseStatusException {

        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(userPk);

        Long matchingInfoId = matchingInfoService.save(request.getDormNum(), request.getJoinPeriod(), request.getGender(), request.getAge(), request.getIsSnoring(), request.getIsGrinding(), request.getIsSmoking(), request.getIsAllowedFood(), request.getIsWearEarphones(), request.getWakeUpTime(), request.getCleanUpStatus(), request.getShowerTime(), request.getOpenKakaoLink(), request.getMbti(), request.getWishText());
        matchingInfoService.updateMatchingInfoAddMember(matchingInfoId, member);

        return ResponseEntity.status(200)
        .body(DefaultResponseDto.builder()
                .responseCode("OK")
                .responseMessage("온보딩 정보 저장 완료")
                .data(matchingInfoId)
                .build()
        );

//        if(member == null) {
//            return ResponseEntity.status(400)
//                    .body(DefaultResponseDto.builder()
//                    .responseCode("REQUIRED_USER_LOGIN")
//                    .responseMessage("로그인이 필요합니다.")
//                    .build());
//        }

//        if(member.getMatchingInfo() == null) { // 등록된 매칭정보가 없다면
//
//            Long matchingInfoId = matchingInfoService.save(request);
//            matchingInfoService.updateMatchingInfoAddMember(matchingInfoId, member);
//
//            return ResponseEntity.status(200)
//                    .body(DefaultResponseDto.builder()
//                            .responseCode("OK")
//                            .responseMessage("온보딩 정보 저장 완료")
//                            .data(matchingInfoId)
//                            .build()
//                    );
//        } else {
//            throw new IllegalArgumentException("이미 등록된 매칭정보가 있습니다.");
//        }
    }

    /**
     * 온보딩(매칭) 정보 수정
     */

    /**
     * 온보딩(매칭) 정보 조회
     */

    /**
     * 온보딩(매칭) 정보 삭제
     */
    // TODO: Member 탈퇴 시 삭제 처리

}
