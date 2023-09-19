package idorm.idormServer.member.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.member.domain.Email;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.dto.*;
import idorm.idormServer.member.service.EmailService;
import idorm.idormServer.member.service.MemberService;
import idorm.idormServer.member.service.MemberServiceFacade;
import idorm.idormServer.photo.service.MemberPhotoService;
import idorm.idormServer.photo.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static idorm.idormServer.config.SecurityConfiguration.*;
import static idorm.idormServer.exception.ExceptionCode.MEMBER_NOT_FOUND;
import static idorm.idormServer.exception.ExceptionCode.UNAUTHORIZED_EMAIL;

@Tag(name = "8. Member", description = "회원 api")
@Validated
@RestController
@RequestMapping(API_ROOT_URL_V1)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final EmailService emailService;
    private final PhotoService photoService;
    private final MemberPhotoService memberPhotoService;
    private final MemberServiceFacade memberServiceFacade;

    @Operation(summary = "회원 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "MEMBER_FOUND",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> findOneMember(
            HttpServletRequest servletRequest
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);

        MemberResponse response = new MemberResponse(member);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_FOUND")
                        .responseMessage("회원 단건 조회 완료")
                        .data(response)
                        .build());
    }

    @Operation(summary = "회원 가입", description = "- 회원 가입은 이메일 인증이 완료된 후 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "MEMBER_REGISTERED",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_EMAIL"),
            @ApiResponse(responseCode = "404", description = "EMAIL_NOT_FOUND"),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_EMAIL / DUPLICATE_NICKNAME"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(AUTHENTICATION_URL + "/register")
    public ResponseEntity<DefaultResponseDto<Object>> saveMember(
            @RequestBody @Valid MemberSaveRequest request
    ) {

        Email email = emailService.findByEmail(request.getEmail());

        if (!email.getIsCheck()) {
            throw new CustomException(null, UNAUTHORIZED_EMAIL);
        }

        emailService.isExistingRegisteredEmail(email.getEmail());
        memberService.isExistingNickname(request.getNickname());

        Member member = memberServiceFacade.saveMember(request, email);

        MemberResponse response = new MemberResponse(member);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_REGISTERED")
                        .responseMessage("회원 가입 완료")
                        .data(response)
                        .build());
    }

    @Operation(summary = "프로필 사진 저장", description = "- 기존 프로필 사진을 삭제 후 새로운 프로필 사진을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "PROFILE_PHOTO_SAVED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404", description = "FILE_NOT_FOUND"),
            @ApiResponse(responseCode = "413", description = "FILE_SIZE_EXCEED"),
            @ApiResponse(responseCode = "415", description = "FILE_TYPE_UNSUPPORTED"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/member/profile-photo")
    public ResponseEntity<DefaultResponseDto<Object>> saveMemberPhoto(
            HttpServletRequest servletRequest,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);

        photoService.validateFileExistence(file);
        photoService.validateFileType(file);

        memberServiceFacade.saveMemberPhoto(member, file);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("PROFILE_PHOTO_SAVED")
                        .responseMessage("회원 프로필 사진 저장 완료")
                        .build());
    }

    @Operation(summary = "프로필 사진 삭제", description = "- 삭제할 사진이 없다면 404(FILE_NOT_FOUND)를 던집니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "PROFILE_PHOTO_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404", description = "FILE_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @DeleteMapping("/member/profile-photo")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMemberPhoto(
            HttpServletRequest servletRequest) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);

        memberPhotoService.validateMemberPhotoIsExistence(member);
        memberPhotoService.delete(member.getMemberPhoto());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("PROFILE_PHOTO_DELETED")
                        .responseMessage("회원 프로필 사진 삭제 완료")
                        .build());
    }

    @Operation(summary = "비밀번호 변경", description = "- 이메일 API 에서 /verifyCode/password/{email} 인증 후 5분동안 비밀번호 변경 가능합니다.\n" +
            "- 비밀번호 변경용 이메일이 인증되지 않았다면 UNAUTHORIZED_EMAIL(401)을 던집니다. \n" +
            "- 비밀번호 변경용 이메일 인증 성공 시점으로 5분 후에 해당 API 요청 시 EXPIRED_CODE(401)를 던집니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "PASSWORD_UPDATED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_EMAIL / EXPIRED_CODE"),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @PatchMapping(AUTHENTICATION_URL + "/password")
    public ResponseEntity<DefaultResponseDto<Object>> updatePassword(
            @RequestBody @Valid PasswordRequest request) {

        emailService.validateEmailDomain(request.getEmail());

        Email email = emailService.findMemberByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(null, MEMBER_NOT_FOUND));

        emailService.validateIsPossibleUpdatePassword(email);

        memberServiceFacade.updatePassword(email, request);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("PASSWORD_UPDATED")
                        .responseMessage("회원 비밀번호 변경 완료")
                        .build());

    }

    @Operation(summary = "닉네임 변경", description = "- 기존 닉네임과 동일 닉네임으로 변경 요청 시 DUPLICATE_SAME_NICKNAME(409)를 던집니다. \n" +
            "- 요청한 닉네임을 가진 다른 회원 존재 시 409(DUPLICATE_NICKNAME)를 던집니다. \n" +
            "- 30일 이내로 닉네임 변경 기록이 있으면 409(CANNOT_UPDATE_NICKNAME)를 던집니다. \n" +
            "- 닉네임 형식은 영문, 숫자, 또는 한글의 조합 / 2-8 글자 입니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "NICKNAME_UPDATED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / NICKNAME_CHARACTER_INVALID / NICKNAME_LENGTH_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_SAME_NICKNAME / DUPLICATE_NICKNAME / CANNOT_UPDATE_NICKNAME"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @PatchMapping("/member/nickname")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberNickname(
            HttpServletRequest servletRequest,
            @RequestBody @Valid NicknameRequest request) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);

        memberService.validateUpdateNicknameIsChanged(member, request.getNickname());
        memberService.isExistingNickname(request.getNickname());
        memberService.validateNicknameUpdatedAt(member);

        memberService.updateNickname(member, request.getNickname());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("NICKNAME_UPDATED")
                        .responseMessage("회원 닉네임 변경 완료")
                        .build());
    }

    @Operation(summary = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "MEMBER_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @DeleteMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMember(
            HttpServletRequest servletRequest
    ) {
        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);

        memberServiceFacade.deleteMember(member);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_DELETED")
                        .responseMessage("회원 삭제 완료")
                        .build());
    }

    @Operation(summary = "로그인", description = "- 헤더에 토큰을 담아 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "MEMBER_LOGIN",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / EMAIL_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_PASSWORD"),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND / MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @PostMapping(AUTHENTICATION_URL + "/login")
    public ResponseEntity<DefaultResponseDto<Object>> login(
            @RequestHeader(name = "fcm-token", required = false) String fcmToken,
            @RequestBody @Valid LoginRequest request) {

        Member member = memberService.findByEmail(request.getEmail());

        memberService.validatePassword(member, request.getPassword());

        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
        memberService.updateFcmToken(member, fcmToken);

        MemberResponse response = new MemberResponse(member);

        return ResponseEntity.status(200)
                .header(AUTHENTICATION_HEADER_NAME, token)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_LOGIN")
                        .responseMessage("회원 로그인 완료")
                        .data(response)
                        .build());
    }

    @Operation(summary = "로그인 시 FCM 토큰 업데이트",
            description = "- 앱을 실행할 때 로그인이 되어 있으면 타임스탬프 갱신을 위한 FCM 토큰을 서버에 전송해주세요. \n" +
                    "- FCM 토큰이 만료되었을 때 FCM 토큰을 업데이트해주세요.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "MEMBER_FCM_UPDATED"),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / EMAIL_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_PASSWORD"),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND / MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @PatchMapping("/member/fcm")
    public ResponseEntity<DefaultResponseDto<Object>> updateFcmToken(
            HttpServletRequest request,
            @RequestHeader("fcm-token") String fcmToken) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);

        memberService.updateFcmToken(member, fcmToken);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_FCM_UPDATED")
                        .responseMessage("회원 FCM 업데이트 완료")
                        .build());
    }

    @Operation(summary = "로그아웃 / FCM 제거 용도",
            description = "- FCM 토큰 관리를 위해 로그아웃 시 해당 API를 호출해주세요.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "MEMBER_LOGOUT"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_PASSWORD"),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @DeleteMapping("/member/fcm")
    public ResponseEntity<DefaultResponseDto<Object>> logout(
            HttpServletRequest request) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(loginMemberId);

        memberService.deleteFcmToken(member);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_LOGOUT")
                        .responseMessage("회원 로그아웃 완료")
                        .build());
    }
}
