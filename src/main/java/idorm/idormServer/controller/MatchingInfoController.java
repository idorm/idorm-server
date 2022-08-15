package idorm.idormServer.controller;

import idorm.idormServer.dto.MatchingInfoDTO;
import idorm.idormServer.dto.MemberDTO;
import idorm.idormServer.service.MatchingInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static idorm.idormServer.dto.MemberDTO.*;

@RestController
@RequiredArgsConstructor
public class MatchingInfoController {

    private final MatchingInfoService matchingInfoService;

    /**
     * 온보딩(매칭)정보 저장
     */
//    @PostMapping("/matchinginfo")
//    public ReturnMemberIdResponse saveMatchingInfo(@RequestBody @Valid MatchingInfoDTO.CreateMatchingInfoRequest request, HttpServletRequest request2) {
//
//    }

}
