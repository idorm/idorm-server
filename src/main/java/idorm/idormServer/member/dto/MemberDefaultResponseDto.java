package idorm.idormServer.member.dto;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;

import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Optional;

@Getter
@ApiModel(value = "Member 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class MemberDefaultResponseDto {

    @ApiModelProperty(position = 1, required = true, dataType = "Long", value = "식별자", example = "1")
    private Long id;

    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "이메일", example = "aaa@inu.ac.kr")
    private String email;

    @ApiModelProperty(position = 3, dataType = "String", value = "닉네임", example = "현")
    private String nickname;

    @ApiModelProperty(position = 4, dataType = "Long", value = "매칭정보 식별자")
    private Long matchingInfoId;

    @ApiModelProperty(position = 5, dataType = "String", value = "로그인 토큰")
    private String loginToken;

    public MemberDefaultResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();

        if(member.getMatchingInfo() != null) {
            this.matchingInfoId = member.getMatchingInfo().getId();
        }
    }

    public MemberDefaultResponseDto(Member member, String token) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.loginToken = token;

        if(member.getMatchingInfo() != null) {
            this.matchingInfoId = member.getMatchingInfo().getId();
        }

    }
}