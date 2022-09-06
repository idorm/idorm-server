package idorm.idormServer.dto;

import idorm.idormServer.domain.MatchingInfo;
import idorm.idormServer.domain.Member;
//import idorm.idormServer.domain.MatchingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public class MemberDTO {

    @Data
    public static class MemberOneDto {

        private Long id;
        private String nickname;
        private String email;
        // matchingInfo
        private Long matchingInfoId;

        public MemberOneDto(Member member) {
            id = member.getId();
            nickname = member.getNickname();
            email = member.getEmail();

            Optional<MatchingInfo> matchingInfo = Optional.ofNullable(member.getMatchingInfo());
            if(!matchingInfo.isEmpty()) {
                matchingInfoId = matchingInfo.get().getId();
            }
        }
    }

    @Data
    public static class ReturnMemberIdResponse {

        private Long id;
        public ReturnMemberIdResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    public static class UpdateMemberRequest {

        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        private String password;
        @NotBlank(message = "닉네임 입력은 필수입니다.")
        private String nickname;
    }

    @Data
    public static class UpdateMemberPasswordRequest {

        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        private String password;
    }

    @Data
    public static class UpdateMemberNicknameRequest {

        @NotBlank(message = "닉네임 입력은 필수입니다.")
        private String nickname;
    }

    @Data
    public static class LoginMemberRequest {

        @NotBlank(message = "이메일 입력은 필수입니다.")
        private String email;

        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        private String password;
    }

    @Data
    public static class CreateMemberRequest {

        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        private String password;

        @Email
        @NotBlank(message = "이메일 입력은 필수입니다.")
        private String email;
    }

    @Data
    @AllArgsConstructor
    public static class DeleteMember {

        private Long id;
    }

}
