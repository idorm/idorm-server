package idorm.idormServer.email.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "Email 기본 응답")
public class EmailDefaultResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "식별자", example = "2")
    private Long id;

    @ApiModelProperty(position = 2, required = true, value = "이메일", example = "aaa@inu.ac.kr")
    private String email;

    @ApiModelProperty(position = 3, required = true, value = "인증번호", example = "123-456")
    private String verifyCode;

    @ApiModelProperty(position = 4, required = true, value = "인증여부", example = "true")
    private boolean isCheck;

    @ApiModelProperty(position = 5, required = true, value = "가입여부", example = "false")
    private boolean isJoin;

    @ApiModelProperty(position = 6, required = true, value = "생성일", example = "시간")
    private LocalDateTime createdAt;

    @ApiModelProperty(position = 7, required = true, value = "수정일", example = "시간")
    private LocalDateTime updatedAt;
}