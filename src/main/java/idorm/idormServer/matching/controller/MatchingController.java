package idorm.idormServer.matching.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exception.CustomException;

import idorm.idormServer.matching.dto.MatchingDefaultResponseDto;
import idorm.idormServer.matching.dto.MatchingFilteredMatchingInfoRequestDto;
import idorm.idormServer.matching.service.MatchingService;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

import static idorm.idormServer.exception.ExceptionCode.*;

@Api(tags = "매칭")
@RestController
@RequiredArgsConstructor
public class MatchingController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final MatchingInfoService matchingInfoService;
    private final MatchingService matchingService;

    @ApiOperation(value = "좋아요한 회원 다건 조회", notes = "매칭이미지 공개 여부가 false 인 매칭정보는 클라이언트에서 추가 필터링이 필요합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "LIKEDMEMBERS_FOUND",
                    content = @Content(schema = @Schema(implementation = MatchingDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "LIKEDMEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping("/member/matching/like")
    public ResponseEntity<DefaultResponseDto<Object>> findLikedMatchingMembers(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        List<Member> likedMembers = matchingService.findLikedMembers(loginMember);

        List<MatchingDefaultResponseDto> response = new ArrayList<>();

        if (likedMembers != null) {
            for(Member likedMember : likedMembers) {
                response.add(new MatchingDefaultResponseDto(likedMember.getMatchingInfo()));
            }
        }
        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("LIKEDMEMBERS_FOUND")
                        .responseMessage("Matching 좋아요한 회원 다건 조회 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "싫어요한 회원 다건 조회", notes = "매칭이미지 공개 여부가 false 인 매칭정보는 추가 필터링이 필요합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "DISLIKEDMEMBERS_FOUND",
                    content = @Content(schema = @Schema(implementation = MatchingDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "DISLIKEDMEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping("/member/matching/dislike")
    public ResponseEntity<DefaultResponseDto<Object>> findDislikedMatchingMembers(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        List<Member> dislikedMembers = matchingService.findDislikedMembers(loginMember);

        List<MatchingDefaultResponseDto> response = new ArrayList<>();

        if (dislikedMembers != null) {
            for(Member dislikedMember : dislikedMembers) {
                response.add(new MatchingDefaultResponseDto(dislikedMember.getMatchingInfo()));
            }
        }
        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("DISLIKEDMEMBERS_FOUND")
                        .responseMessage("Matching 싫어요한 회원 다건 조회 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "매칭 좋아요 혹은 싫어요한 회원 추가",
            notes = "- matchingType true는 좋아요한 회원, false는 싫어요한 회원을 의미합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "LIKEDMEMBER_SAVED / DISLIKEDMEMBER_SAVED"),
            @ApiResponse(responseCode = "400",
                    description = "SELECTEDMEMBERID_NEGATIVEORZERO_INVALID / ILLEGAL_ARGUMENT_SELF" +
                            " / ILLEGAL_ARGUMENT_ADMIN / ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND / MATCHINGINFO_NOT_FOUND"),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_LIKED_MEMBER / DUPLICATE_DISLIKED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/member/matching/{selected-member-id}")
    public ResponseEntity<DefaultResponseDto<Object>> addMatchingMember(
            HttpServletRequest request,
            @RequestParam(value = "matchingType")
                    boolean matchingType,
            @PathVariable(value = "selected-member-id")
            @Positive(message = "추가할 회원 식별자는 양수만 가능합니다.")
                    Long selectedMemberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);
        Member selectedMember = memberService.findById(selectedMemberId);

        matchingInfoService.findByMemberId(loginMemberId);
        matchingInfoService.findByMemberId(selectedMemberId);

        matchingInfoService.validateMatchingInfoIsPublic(loginMember);
        matchingInfoService.validateMatchingInfoIsPublic(selectedMember);

        if(loginMember.equals(selectedMember)) {
            throw new CustomException(ILLEGAL_ARGUMENT_SELF);
        }
        if(selectedMember.getRoles().contains("ROLE_ADMIN")) {
            throw new CustomException(ILLEGAL_ARGUMENT_ADMIN);
        }

        if (matchingType == true) {
            matchingService.addLikedMember(loginMember, selectedMemberId);

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("LIKEDMEMBER_SAVED")
                            .responseMessage("Matching 좋아요한 회원 추가 완료")
                            .build());
        } else {
            matchingService.addDislikedMember(loginMember, selectedMemberId);

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("DISLIKEDMEMBER_SAVED")
                            .responseMessage("Matching 싫어요한 회원 추가 완료")
                            .build());
        }
    }

    @ApiOperation(value = "매칭 좋아요 혹은 싫어요한 회원 삭제",
            notes = "- matchingType true는 좋아요한 회원, false는 싫어요한 회원을 의미합니다. ")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "LIKEDMEMBER_DELETED / DISLIKEDMEMBER_DELETED"),
            @ApiResponse(responseCode = "400",
                    description = "SELECTEDMEMBERID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "LIKEDMEMBER_NOT_FOUND / DISLIKEDMEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @DeleteMapping("/member/matching/{selected-member-id}")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMatchingMember(
            HttpServletRequest request,
            @RequestParam(value = "matchingType")
                    boolean matchingType,
            @PathVariable(value = "selected-member-id")
            @Positive(message = "추가할 회원 식별자는 양수만 가능합니다.")
                    Long selectedMemberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        if (matchingType == true) {
            matchingService.removeLikedMember(loginMember, selectedMemberId);

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("LIKEDMEMBER_DELETED")
                            .responseMessage("Matching 좋아요한 회원 삭제 완료")
                            .build());
        } else {
            matchingService.removeDislikedMember(loginMember, selectedMemberId);

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("DISLIKEDMEMBER_DELETED")
                            .responseMessage("Matching 싫어요한 회원 삭제 완료")
                            .build());
        }
    }

    @ApiOperation(value = "매칭 회원 다건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "MATCHING_MEMBERS_FOUND",
                    content = @Content(schema = @Schema(implementation = MatchingDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "MATCHINGINFO_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping("/member/matching")
    public ResponseEntity<DefaultResponseDto<Object>> findMatchingMembers(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);
        matchingInfoService.findByMemberId(loginMemberId);
        matchingInfoService.validateMatchingInfoIsPublic(loginMember);

        List<MatchingInfo> foundMatchingInfo = matchingService.findMatchingMembers(loginMember);

        List<MatchingDefaultResponseDto> responses = new ArrayList<>();

        if (foundMatchingInfo != null) {
            for(MatchingInfo matchingInfo : foundMatchingInfo) {
                responses.add(new MatchingDefaultResponseDto(matchingInfo));
            }
        }
        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MATCHING_MEMBERS_FOUND")
                        .responseMessage("Matching 매칭 회원 조회 완료")
                        .data(responses)
                        .build());
    }

    @ApiOperation(value = "필터링 매칭 회원 다건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "FILTERED_MATCHING_MEMBERS_FOUND",
                    content = @Content(schema = @Schema(implementation = MatchingDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / DORMCATEGORY_CHARACTER_INVALID / " +
                            "JOINPERIOD_CHARACTER_INVALID / ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "MATCHINGINFO_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/member/matching/filter")
    public ResponseEntity<DefaultResponseDto<Object>> findFilteredMatchingMembers(
            HttpServletRequest request2,
            @RequestBody @Valid MatchingFilteredMatchingInfoRequestDto request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);
        matchingInfoService.findByMemberId(loginMemberId);
        matchingInfoService.validateMatchingInfoIsPublic(loginMember);

        List<MatchingInfo> foundMatchingInfos = matchingService.findFilteredMatchingMembers(loginMember, request);

        List<MatchingDefaultResponseDto> responses = new ArrayList<>();

        if (foundMatchingInfos != null) {
            for(MatchingInfo matchingInfo : foundMatchingInfos) {
                responses.add(new MatchingDefaultResponseDto(matchingInfo));
            }
        }

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("FILTERED_MATCHING_MEMBERS_FOUND")
                        .responseMessage("Matching 필터링 후 매칭 회원 조회 완료")
                        .data(responses)
                        .build());
    }

}