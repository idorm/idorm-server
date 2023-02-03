package idorm.idormServer.member.dto;

import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel(value = "Member 저장 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSaveRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "이메일", example = "aaa@inu.ac.kr")
    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @ApiModelProperty(position = 2, required = true, value = "비밀번호", example = "aaaaaaa7!")
    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8-15자만 가능합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$",
            message = "비밀번호는 영소문자, 숫자, 특수문자는 필수이고 대문자는 선택인 형식만 가능합니다.")
    private String password;

    @ApiModelProperty(position = 3, required = true, value = "닉네임", example = "에러난응철이")
    @NotBlank(message = "닉네임 입력은 필수입니다.")
    @Size(min = 2, max = 8, message = "닉네임은 2-8자만 가능합니다.")
    @Pattern(regexp = "^[A-Za-z0-9ㄱ-ㅎ가-힣]+$",
            message = "닉네임은 영문, 숫자, 또는 한글의 조합 형식만 가능합니다.")
    private String nickname;

    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .build();

    }
}