package idorm.idormServer.matching.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.matching.dto.MatchingDefaultResponseDto;
import idorm.idormServer.matching.service.MatchingService;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Matching API")
public class MatchingController {

    private final MatchingService matchingService;
    private final MatchingInfoService matchingInfoService;

    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "Matching 매칭멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Matching 매칭멤버 조회 완료"),
            @ApiResponse(code = 204, message = "매칭되는 멤버가 없습니다."),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "매칭정보가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Matching 매칭멤버 조회 중 서버 에러 발생")
    })
    @GetMapping("/member/matching")
    public ResponseEntity<DefaultResponseDto<Object>> findMatchingMembers(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));

        List<Long> filteredMatchingInfoId = matchingService.findMatchingMembers(loginMemberId);

        if(filteredMatchingInfoId.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("매칭되는 멤버가 없습니다.")
                            .build());
        }

        List<MatchingDefaultResponseDto> response = new ArrayList<>();

        for(Long matchingInfoId : filteredMatchingInfoId) {
            MatchingInfo matchingInfo = matchingInfoService.findById(matchingInfoId);
            MatchingDefaultResponseDto matchingOneDto = new MatchingDefaultResponseDto(matchingInfo);
            response.add(matchingOneDto);
        }
        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Matching 매칭멤버 조회 완료")
                        .data(response)
                        .build());
    }
}
