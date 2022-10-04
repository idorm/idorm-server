package idorm.idormServer.matching.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exceptions.http.UnauthorizedException;
import idorm.idormServer.matching.dto.MatchingDefaultResponseDto;
import idorm.idormServer.matching.dto.MatchingFilteredMatchingInfoRequestDto;
import idorm.idormServer.matching.service.DislikedMemberService;
import idorm.idormServer.matching.service.LikedMemberService;
import idorm.idormServer.matching.service.MatchingService;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
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
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Matching API")
public class MatchingController {

    private final MatchingService matchingService;
    private final MatchingInfoService matchingInfoService;
    private final LikedMemberService likedMemberService;
    private final DislikedMemberService dislikedMemberService;

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
            @ApiResponse(code = 204, message = "Matching 좋아요한 매칭멤버가 존재하지 않습니다."),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "매칭정보가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Matching 좋아요한 매칭멤버 조회 중 서버 에러 발생")
    })
    @GetMapping("/member/matchinglikedmembers")
    public ResponseEntity<DefaultResponseDto<Object>> findLikedMatchingMembers(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));

        List<Long> likedMembers = likedMemberService.findLikedMembers(loginMemberId);

        if(likedMembers.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("Matching 좋아요한 매칭멤버가 존재하지 않습니다.")
                            .build());
        }

        List<MatchingDefaultResponseDto> response = new ArrayList<>();

        for(Long likedMemberId : likedMembers) {

            Long likedMemberMatchingInfoId = matchingInfoService.findByMemberId(likedMemberId); // 에러 발생
            MatchingInfo likedMemberMatchingInfo = matchingInfoService.findById(likedMemberMatchingInfoId);

            MatchingDefaultResponseDto matchingOneDto = new MatchingDefaultResponseDto(likedMemberMatchingInfo);
            response.add(matchingOneDto);
        }
        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Matching 좋아요한 매칭멤버 조회 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "Matching 좋아요한 매칭멤버 추가")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Matching 좋아요한 매칭멤버 추가 완료"),
            @ApiResponse(code = 401, message = "해당 멤버의 id를 좋아요한 멤버로 설정할 수 없습니다."),
            @ApiResponse(code = 409, message = "매칭정보가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "LikedMember save 중 서버 에러 발생")
    })
    @PostMapping("/member/matchinglikedmembers")
    public ResponseEntity<DefaultResponseDto<Object>> addLikedMatchingMembers(
            HttpServletRequest request, Long selectedLikedMemberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));

        if(selectedLikedMemberId == loginMemberId || selectedLikedMemberId == 1) {
            throw new UnauthorizedException("관리자 혹은 본인을 좋아요한 멤버로 설정할 수 없습니다.");
        }

        likedMemberService.saveLikedMember(loginMemberId, selectedLikedMemberId);

        Long loginMemberMatchingInfoId = matchingInfoService.findByMemberId(loginMemberId);
        MatchingInfo loginMemberMatchingInfo = matchingInfoService.findById(loginMemberMatchingInfoId);

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
            @ApiResponse(code = 500, message = "Matching 좋아요한 매칭멤버 삭제 중 서버 에러 발생")
    })
    @DeleteMapping("/member/matchinglikedmembers")
    public ResponseEntity<DefaultResponseDto<Object>> deleteLikedMatchingMembers(
            HttpServletRequest request, @NotBlank Long likedMemberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));

        likedMemberService.deleteLikedMember(loginMemberId, likedMemberId);

        Long loginMemberMatchingInfoId = matchingInfoService.findByMemberId(loginMemberId);
        MatchingInfo loginMemberMatchingInfo = matchingInfoService.findById(loginMemberMatchingInfoId);
        MatchingDefaultResponseDto response = new MatchingDefaultResponseDto(loginMemberMatchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Matching 좋아요한 매칭멤버 삭제 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "Matching 싫어요한 매칭멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Matching 싫어요한 매칭멤버 조회 완료"),
            @ApiResponse(code = 204, message = "Matching 싫어요한 매칭멤버가 존재하지 않습니다."),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "매칭정보가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Matching 싫어요한 매칭멤버 조회 중 서버 에러 발생")
    })
    @GetMapping("/member/matchingdislikedmembers")
    public ResponseEntity<DefaultResponseDto<Object>> findDislikedMatchingMembers(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));

        List<Long> dislikedMembers = dislikedMemberService.findDislikedMembers(loginMemberId);

        if(dislikedMembers.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("Matching 싫어요한 매칭멤버가 존재하지 않습니다.")
                            .build());
        }

        List<MatchingDefaultResponseDto> response = new ArrayList<>();

        for(Long dislikedMemberId : dislikedMembers) {

            Long dislikedMemberMatchingInfoId = matchingInfoService.findByMemberId(dislikedMemberId);
            MatchingInfo dislikedMemberMatchingInfo = matchingInfoService.findById(dislikedMemberMatchingInfoId);

            MatchingDefaultResponseDto matchingOneDto = new MatchingDefaultResponseDto(dislikedMemberMatchingInfo);
            response.add(matchingOneDto);
        }
        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Matching 싫어요한 매칭멤버 조회 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "Matching 싫어요한 매칭멤버 추가")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Matching 싫어요한 매칭멤버 추가 완료"),
            @ApiResponse(code = 401, message = "해당 멤버의 id를 싫어요한 멤버로 설정할 수 없습니다."),
            @ApiResponse(code = 409, message = "매칭정보가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "DislikedMember save 중 서버 에러 발생")
    })
    @PostMapping("/member/matchingdislikedmembers")
    public ResponseEntity<DefaultResponseDto<Object>> addDislikedMatchingMembers(
            HttpServletRequest request, @NotBlank Long selectedDislikedMemberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));

        if(selectedDislikedMemberId == loginMemberId || selectedDislikedMemberId == 1) {
            throw new UnauthorizedException("관리자 혹은 본인을 싫어요한 멤버로 설정할 수 없습니다.");
        }

        dislikedMemberService.saveDislikedMember(loginMemberId, selectedDislikedMemberId);

        Long loginMemberMatchingInfoId = matchingInfoService.findByMemberId(loginMemberId);
        MatchingInfo loginMemberMatchingInfo = matchingInfoService.findById(loginMemberMatchingInfoId);

        MatchingDefaultResponseDto response = new MatchingDefaultResponseDto(loginMemberMatchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Matching 싫어요한 매칭멤버 추가 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "Matching 싫어요한 매칭멤버 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Matching 싫어요한 매칭멤버 삭제 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 404, message = "싫어요한 멤버의 id가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Matching 싫어요한 매칭멤버 삭제 중 서버 에러 발생")
    })
    @DeleteMapping("/member/matchingdislikedmembers")
    public ResponseEntity<DefaultResponseDto<Object>> deleteDislikedMatchingMembers(
            HttpServletRequest request, @NotBlank Long dislikedMemberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));

        dislikedMemberService.deleteDislikedMember(loginMemberId, dislikedMemberId);

        Long loginMemberMatchingInfoId = matchingInfoService.findByMemberId(loginMemberId);
        MatchingInfo loginMemberMatchingInfo = matchingInfoService.findById(loginMemberMatchingInfoId);
        MatchingDefaultResponseDto response = new MatchingDefaultResponseDto(loginMemberMatchingInfo);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Matching 싫어요한 매칭멤버 삭제 완료")
                        .data(response)
                        .build());
    }




}