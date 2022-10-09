package idorm.idormServer.email.dto;

import idorm.idormServer.email.domain.Email;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@ApiModel(value = "Email 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailDefaultResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "식별자")
    private Long id;

    @ApiModelProperty(position = 2, required = true, value = "이메일")
    private String email;

    @ApiModelProperty(position = 3, required = true, value = "인증번호")
    private String verifyCode;

    @ApiModelProperty(position = 4, required = true, value = "인증여부")
    private boolean isCheck;

    @ApiModelProperty(position = 5, required = true, value = "가입여부")
    private boolean isJoin;

    @ApiModelProperty(position = 6, required = true, value = "생성일")
    private LocalDateTime createdAt;

    @ApiModelProperty(position = 7, required = true, value = "수정일")
    private LocalDateTime updatedAt;

    public EmailDefaultResponseDto(Email email){
        this.id = email.getId();
        this.email = email.getEmail();
        this.verifyCode = email.getCode();
        this.isCheck = email.isCheck();
        this.isJoin = email.isJoin();
        this.createdAt = email.getCreatedAt();
        this.updatedAt = email.getUpdatedAt();
    }
}