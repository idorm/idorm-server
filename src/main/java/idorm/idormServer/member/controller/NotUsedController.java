package idorm.idormServer.member.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.dto.MemberDefaultResponseDto;
import idorm.idormServer.member.dto.MemberLoginRequestDto;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static idorm.idormServer.exception.ExceptionCode.UNAUTHORIZED_PASSWORD;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Api(tags = "삭제 예정")
@Validated
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NotUsedController {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    @Value("${DB_NAME}")
    private String ENV_USERNAME;

    @Value("${ADMIN_PASSWORD}")
    private String ENV_PASSWORD;

    @ApiOperation(value = "[삭제 예정] 로그인", notes = "- 헤더에 토큰을 담아 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "MEMBER_LOGIN",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / EMAIL_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_PASSWORD"),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND / MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    }
    )
    @PostMapping("/login")
    public ResponseEntity<DefaultResponseDto<Object>> login(
            @RequestBody @Valid MemberLoginRequestDto request) {

        Member loginMember = null;

        if(request.getEmail().equals(ENV_USERNAME + "@inu.ac.kr")) {
            if (!passwordEncoder.matches(request.getPassword(), passwordEncoder.encode(ENV_PASSWORD))) {
                throw new CustomException(null, UNAUTHORIZED_PASSWORD);
            }
            loginMember = memberService.findById(1L);
        } else {
            loginMember = memberService.findByEmail(request.getEmail());

            if (!passwordEncoder.matches(request.getPassword(), loginMember.getPassword())) {
                throw new CustomException(null, UNAUTHORIZED_PASSWORD);
            }
        }

        Iterator<String> iter = loginMember.getRoles().iterator();
        List<String> roles = new ArrayList<>();

        while (iter.hasNext()) {
            roles.add(iter.next());
        }

        String token = jwtTokenProvider.createToken(loginMember.getUsername(), roles);
        MemberDefaultResponseDto response = new MemberDefaultResponseDto(loginMember);

        return ResponseEntity.status(200)
                .header(AUTHORIZATION, token)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_LOGIN")
                        .responseMessage("회원 로그인 완료")
                        .data(response)
                        .build());
    }
}
