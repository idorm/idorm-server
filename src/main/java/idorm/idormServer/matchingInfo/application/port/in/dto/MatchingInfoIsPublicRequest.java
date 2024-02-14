//package idorm.idormServer.matchingInfo.dto;
//
//import idorm.idormServer.common.ValidationSequence;
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.validation.GroupSequence;
//import javax.validation.constraints.NotNull;
//
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@GroupSequence({MatchingInfoIsPublicRequest.class,
//        ValidationSequence.NotNull.class
//})
//@Schema(title = "온보딩 정보 공개 여부 수정 요청")
//public class MatchingInfoIsPublicRequest {
//
//    @Schema(required = true, description = "매칭 이미지 공개 여부", example = "true")
//    @NotNull(message = "매칭 이미지 공개 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
//    private Boolean isMatchingInfoPublic;
//}
