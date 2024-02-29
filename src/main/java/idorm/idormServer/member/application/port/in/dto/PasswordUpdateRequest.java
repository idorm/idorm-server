package idorm.idormServer.member.application.port.in.dto;

public record PasswordUpdateRequest(
//        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        String email,
//        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        String password
) {
}
