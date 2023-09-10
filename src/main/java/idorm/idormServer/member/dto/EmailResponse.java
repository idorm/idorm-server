package idorm.idormServer.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(title = "Email 기본 응답")
public class EmailResponse {

    @Schema(required = true, description = "식별자", example = "2")
    private Long id;

    @Schema(required = true, description = "이메일", example = "test1@inu.ac.kr")
    private String email;

    @Schema(required = true, description = "인증번호", example = "123-456")
    private String verifyCode;

    @Schema(required = true, description = "인증여부", example = "true")
    private boolean isCheck;

    @Schema(required = true, description = "가입여부", example = "false")
    private boolean isJoin;

    @Schema(required = true, description = "생성일", example = "시간")
    private LocalDateTime createdAt;

    @Schema(required = true, description = "수정일", example = "시간")
    private LocalDateTime updatedAt;
}