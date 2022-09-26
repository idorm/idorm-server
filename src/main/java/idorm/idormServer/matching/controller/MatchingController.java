package idorm.idormServer.matching.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exceptions.http.NotFoundException;
import idorm.idormServer.matching.dto.MatchingDefaultResponseDto;
import idorm.idormServer.matching.dto.MatchingFilteredMatchingInfoRequestDto;
import idorm.idormServer.matching.service.MatchingService;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Api(tags = "Matching API")
public class MatchingController {

    private final MatchingService matchingService;
    private final MatchingInfoService matchingInfoService;
    private final MemberService memberService;

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

    @ApiOperation(value = "Matching 필터링된 매칭멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Matching 필터링된 매칭멤버 조회 완료"),
            @ApiResponse(code = 204, message = "매칭되는 멤버가 없습니다."),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "매칭정보가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Matching 매칭멤버 조회 중 서버 에러 발생")
    })
    @GetMapping("/member/matchingfiltered")
    public ResponseEntity<DefaultResponseDto<Object>> findFilteredMatchingMembers(
            HttpServletRequest request, MatchingFilteredMatchingInfoRequestDto filteringRequest
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));

        List<Long> filteredMatchingInfoId = matchingService.findFilteredMatchingMembers(loginMemberId, filteringRequest);

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
                        .responseMessage("Matching 필터링된 매칭멤버 조회 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "Matching 좋아요한 매칭멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Matching 좋아요한 매칭멤버 조회 완료"),
            @ApiResponse(code = 204, message = "매칭되는 멤버가 없습니다."),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "매칭정보가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Matching 좋아요한 매칭멤버 조회 중 서버 에러 발생")
    })
    @GetMapping("/member/matchinglikedmembers")
    public ResponseEntity<DefaultResponseDto<Object>> findLikedMatchingMembers(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));

        List<MatchingInfo> matchingInfoList = matchingService.findMatchingLikedMembers(loginMemberId);

        if(matchingInfoList.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("매칭되는 멤버가 없습니다.")
                            .build());
        }

        List<MatchingDefaultResponseDto> response = new ArrayList<>();

        for(MatchingInfo matchingInfo : matchingInfoList) {

            MatchingDefaultResponseDto matchingOneDto = new MatchingDefaultResponseDto(matchingInfo);
            response.add(matchingOneDto);
        }
        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Matching 좋아요한 매칭멤버 조회 완료")
                        .data(response)
                        .build());
    }

    // TODO: DB에 좋아요한 멤버 리스트가 조회되지 않아서 해결해야 함

    @ApiOperation(value = "Matching 좋아요한 매칭멤버 추가")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Matching 좋아요한 매칭멤버 추가 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 404, message = "좋아요한 멤버의 id가 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "매칭정보가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Matching 좋아요한 매칭멤버 추가 중 서버 에러 발생")
    })
    @PostMapping("/member/matchinglikedmembers")
    public ResponseEntity<DefaultResponseDto<Object>> addLikedMatchingMembers(
            HttpServletRequest request, Long likedMemberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        Optional<Member> likedMember = Optional.ofNullable(memberService.findById(likedMemberId));

        if(likedMember.isEmpty()) {
            throw new NotFoundException("좋아요한 멤버의 id가 존재하지 않습니다.");
        }

        matchingService.addMatchingLikedMember(loginMemberId, likedMemberId);
        // TODO: 여기
        MatchingInfo loginMemberMatchingInfo = loginMember.getMatchingInfo();
        MatchingDefaultResponseDto response = new MatchingDefaultResponseDto(loginMemberMatchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Matching 좋아요한 매칭멤버 추가 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "Matching 좋아요한 매칭멤버 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Matching 좋아요한 매칭멤버 삭제 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 404, message = "좋아요한 멤버의 id가 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "매칭정보가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Matching 좋아요한 매칭멤버 삭제 중 서버 에러 발생")
    })
    @DeleteMapping("/member/matchinglikedmembers")
    public ResponseEntity<DefaultResponseDto<Object>> deleteLikedMatchingMembers(
            HttpServletRequest request, Long likedMemberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(likedMemberId);
        Optional<Member> likedMember = Optional.ofNullable(memberService.findById(likedMemberId));

        if(likedMember.isEmpty()) {
            throw new NotFoundException("좋아요한 멤버의 id가 존재하지 않습니다.");
        }

        matchingService.deleteMatchingLikedMember(loginMemberId, likedMemberId);

        MatchingInfo loginMemberMatchingInfo = loginMember.getMatchingInfo();
        MatchingDefaultResponseDto response = new MatchingDefaultResponseDto(loginMemberMatchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Matching 좋아요한 매칭멤버 삭제 완료")
                        .data(response)
                        .build());
    }

}