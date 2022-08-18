package idorm.idormServer.controller;

import idorm.idormServer.dto.MatchingInfoDTO;
import idorm.idormServer.dto.MemberDTO;
import idorm.idormServer.jwt.JwtTokenProvider;
import idorm.idormServer.service.MatchingInfoService;
import idorm.idormServer.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static idorm.idormServer.dto.MatchingInfoDTO.*;
import static idorm.idormServer.dto.MemberDTO.*;

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
    public ReturnMatchingInfoIdResponse saveMatchingInfo(@RequestBody @Valid CreateMatchingInfoRequest request, HttpServletRequest request2) {

        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));

        if(memberService.findById(userPk).getMatchingInfo() == null) {
            return new ReturnMatchingInfoIdResponse(
                    matchingInfoService.save(request.getDormNum(), request.getJoinPeriod(), request.getGender(), request.getAge(), request.getIsSnoring(),
                            request.getIsSmoking(), request.getIsGrinding(), request.getIsWearEarphones(), request.getIsAllowedFood(), request.getWakeUpTime(),
                            request.getCleanUpStatus(), request.getShowerTime(), request.getMbti(), request.getWishText(), request.getOpenKakaoLink())
            );
        } else {
            throw new IllegalArgumentException("이미 등록된 매칭정보가 있습니다.");
        }
    }

}
