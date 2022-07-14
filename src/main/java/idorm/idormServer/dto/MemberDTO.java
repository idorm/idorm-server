package idorm.idormServer.dto;

import idorm.idormServer.domain.Member;
import lombok.Data;

public class MemberDTO {

    @Data
    public static class MemberOneDTO {
        private Long id;
        private String email;


        public MemberOneDTO(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();

        }
    }

}
