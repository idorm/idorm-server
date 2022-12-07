package idorm.idormServer.matching.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exceptions.CustomException;
import idorm.idormServer.exceptions.ErrorResponse;
import idorm.idormServer.matching.domain.DislikedMember;
import idorm.idormServer.matching.domain.LikedMember;
import idorm.idormServer.matching.dto.MatchingDefaultResponseDto;
import idorm.idormServer.matching.dto.MatchingFilteredMatchingInfoRequestDto;
import idorm.idormServer.matching.service.DislikedMemberService;
import idorm.idormServer.matching.service.LikedMemberService;
import idorm.idormServer.matching.service.MatchingService;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static idorm.idormServer.exceptions.ErrorCode.*;

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
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MatchingDefaultResponseDto.class))),
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "400",
                    description = "ILLEGAL_STATEMENT_MATCHING_INFO_NON_PUBLIC",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "MATCHING_INFO_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/member/matching")
    public ResponseEntity<DefaultResponseDto<Object>> findMatchingMembers(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));

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
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MatchingDefaultResponseDto.class))),
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / DORM_CATEGORY_FORMAT_INVALID / " +
                            "JOIN_PERIOD_FORMAT_INVALID / ILLEGAL_STATEMENT_MATCHING_INFO_NON_PUBLIC",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "MATCHING_INFO_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/member/matching/filter")
    public ResponseEntity<DefaultResponseDto<Object>> findFilteredMatchingMembers(
            HttpServletRequest request, @RequestBody @Valid MatchingFilteredMatchingInfoRequestDto filteringRequest
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));

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
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MatchingDefaultResponseDto.class))),
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "MATCHING_INFO_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/member/matching/like")
    public ResponseEntity<DefaultResponseDto<Object>> findLikedMatchingMembers(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));

        List<LikedMember> likedMembers = likedMemberService.findLikedMembersByMemberId(loginMemberId);

        if(likedMembers.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("Matching 좋아요한 매칭멤버가 존재하지 않습니다.")
                            .build());
        }

        List<MatchingDefaultResponseDto> response = new ArrayList<>();

        for(LikedMember likedMember : likedMembers) {

            Long likedMemberMatchingInfoId = matchingInfoService.findByMemberId(likedMember.getSelectedLikedMemberId());
            MatchingInfo likedMemberMatchingInfo = matchingInfoService.findById(likedMemberMatchingInfoId);

            MatchingDefaultResponseDto matchingOneDto = new MatchingDefaultResponseDto(likedMemberMatchingInfo,
                    likedMember.getCreatedAt());
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
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / ILLEGAL_ARGUMENT_SELF / ILLEGAL_ARGUMENT_ADMIN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND / MATCHING_INFO_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/member/matching/like/{liked-member-id}")
    public ResponseEntity<DefaultResponseDto<Object>> addLikedMatchingMember(
            HttpServletRequest request, @PathVariable(value = "liked-member-id") Long selectedLikedMemberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));

        if(selectedLikedMemberId == loginMemberId) {
            throw new CustomException(ILLEGAL_ARGUMENT_SELF);
        }
        if(selectedLikedMemberId == 1) {
            throw new CustomException(ILLEGAL_ARGUMENT_ADMIN);
        }

        // 싫어요한 멤버로 등록되어있는지 확인, 되어있다면 true 반환
        boolean isRegisteredDislikedMember =
                dislikedMemberService.isRegisteredDislikedMemberIdByMemberId(loginMemberId, selectedLikedMemberId);

        //  등록되어있다면 싫어요한 멤버 삭제 처리 필요
        if(isRegisteredDislikedMember == true) {
            dislikedMemberService.deleteDislikedMember(loginMemberId, selectedLikedMemberId);
        }
        likedMemberService.saveLikedMember(loginMemberId, selectedLikedMemberId);

        Long loginMemberMatchingInfoId = matchingInfoService.findByMemberId(loginMemberId);
        MatchingInfo loginMemberMatchingInfo = matchingInfoService.findById(loginMemberMatchingInfoId);

        MatchingDefaultResponseDto response = new MatchingDefaultResponseDto(loginMemberMatchingInfo);

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("NO_CONTENT")
                        .responseMessage("Matching 좋아요한 매칭멤버 추가 완료")
                        .build());
    }

    @ApiOperation(value = "Matching 좋아요한 매칭멤버 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/member/matching/like/{liked-member-id}")
    public ResponseEntity<DefaultResponseDto<Object>> deleteLikedMatchingMember(
            HttpServletRequest request, @PathVariable(value = "liked-member-id") Long likedMemberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));

        likedMemberService.deleteLikedMember(loginMemberId, likedMemberId);

        Long loginMemberMatchingInfoId = matchingInfoService.findByMemberId(loginMemberId);
        MatchingInfo loginMemberMatchingInfo = matchingInfoService.findById(loginMemberMatchingInfoId);
        MatchingDefaultResponseDto response = new MatchingDefaultResponseDto(loginMemberMatchingInfo);

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("NO_CONTENT")
                        .responseMessage("Matching 좋아요한 매칭멤버 삭제 완료")
                        .build());
    }

    @ApiOperation(value = "Matching 싫어요한 매칭멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MatchingDefaultResponseDto.class))),
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "MATCHING_INFO_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/member/matching/dislike")
    public ResponseEntity<DefaultResponseDto<Object>> findDislikedMatchingMembers(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));

        List<DislikedMember> dislikedMembers = dislikedMemberService.findDislikedMembersByMemberId(loginMemberId);

        if(dislikedMembers.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("Matching 싫어요한 매칭멤버가 존재하지 않습니다.")
                            .build());
        }

        List<MatchingDefaultResponseDto> response = new ArrayList<>();

        for(DislikedMember dislikedMember : dislikedMembers) {

            Long dislikedMemberMatchingInfoId =
                    matchingInfoService.findByMemberId(dislikedMember.getSelectedDislikedMemberId());

            MatchingInfo dislikedMemberMatchingInfo = matchingInfoService.findById(dislikedMemberMatchingInfoId);

            MatchingDefaultResponseDto matchingOneDto = new MatchingDefaultResponseDto(dislikedMemberMatchingInfo,
                    dislikedMember.getCreatedAt());
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
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / ILLEGAL_ARGUMENT_SELF / ILLEGAL_ARGUMENT_ADMIN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND / MATCHING_INFO_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_DISLIKED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping("/member/matching/dislike/{disliked-member-id}")
    public ResponseEntity<DefaultResponseDto<Object>> addDislikedMatchingMember(
            HttpServletRequest request, @PathVariable(value = "disliked-member-id") Long selectedDislikedMemberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));

        if(selectedDislikedMemberId == loginMemberId) {
            throw new CustomException(ILLEGAL_ARGUMENT_SELF);
        }
        if(selectedDislikedMemberId == 1) {
            throw new CustomException(ILLEGAL_ARGUMENT_ADMIN);
        }

        // 좋아요한 멤버로 등록되어있는지 확인, 되어있다면 true 반환
        boolean isRegisteredLikedMember =
                likedMemberService.isRegisteredlikedMemberIdByMemberId(loginMemberId, selectedDislikedMemberId);

        //  등록되어있다면 좋아요한 멤버 삭제 처리 필요
        if(isRegisteredLikedMember == true) {
            likedMemberService.deleteLikedMember(loginMemberId, selectedDislikedMemberId);
        }
        dislikedMemberService.saveDislikedMember(loginMemberId, selectedDislikedMemberId);

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("NO_CONTENT")
                        .responseMessage("Matching 싫어요한 매칭멤버 추가 완료")
                        .build());
    }

    @ApiOperation(value = "Matching 싫어요한 매칭멤버 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/member/matching/dislike/{disliked-member-id}")
    public ResponseEntity<DefaultResponseDto<Object>> deleteDislikedMatchingMember(
        HttpServletRequest request, @PathVariable(value = "disliked-member-id") Long dislikedMemberId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));

        dislikedMemberService.deleteDislikedMember(loginMemberId, dislikedMemberId);

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("NO_CONTENT")
                        .responseMessage("Matching 싫어요한 매칭멤버 삭제 완료")
                        .build());
    }
}