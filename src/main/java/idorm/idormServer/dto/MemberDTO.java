package idorm.idormServer.dto;

import idorm.idormServer.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

public class MemberDTO {
    private Long id;
    private String email;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
    }

    @Data
    public static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    public static class CreateMemberRequest {

        @NotEmpty
        private String email;
        @NotEmpty
        private String password;

    }

    @Data
    @AllArgsConstructor
    public static class DeleteMember {
        private Long id;
    }

}
