package idorm.idormServer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    /**
     * 400 BAD_REQUEST
     */
    FIELD_REQUIRED(BAD_REQUEST, "입력은 필수 입니다."),
    TARGETS_FIELD_REQUIRED(BAD_REQUEST, "팀 일정 대상자 입력은 필수 입니다."),
    DATE_FIELD_REQUIRED(BAD_REQUEST, "일자 입력은 필수 입니다."),

    // 형식
    EMAIL_CHARACTER_INVALID(BAD_REQUEST, "올바른 형식의 이메일이 아닙니다."),
    PASSWORD_CHARACTER_INVALID(BAD_REQUEST, "올바른 형식의 비밀번호가 아닙니다."),
    NICKNAME_CHARACTER_INVALID(BAD_REQUEST, "올바른 형식의 닉네임이 아닙니다."),
    DORMCATEGORY_CHARACTER_INVALID(BAD_REQUEST, "올바른 형식의 기숙사 분류가 아닙니다."),
    JOINPERIOD_CHARACTER_INVALID(BAD_REQUEST, "올바른 형식의 입사 기간이 아닙니다."),
    GENDER_CHARACTER_INVALID(BAD_REQUEST, "올바른 형식의 성별이 아닙니다."),
    REPORT_TYPE_CHARACTER_INVALID(BAD_REQUEST, "올바른 신고 형식이 아닙니다."),
    MEMBER_REPORT_TYPE_CHARACTER_INVALID(BAD_REQUEST, "올바른 회원 신고 형식이 아닙니다."),
    COMMUNITY_REPORT_TYPE_CHARACTER_INVALID(BAD_REQUEST, "올바른 커뮤니티 신고 형식이 아닙니다."),

    // 사이즈
    NICKNAME_LENGTH_INVALID(BAD_REQUEST, "닉네임은 2~8자만 이내여야 합니다."),
    PASSWORD_LENGTH_INVALID(BAD_REQUEST, "비밀번호는 8~15자 이내여야 합니다."),
    AGE_LENGTH_INVALID(BAD_REQUEST, "나이는 20~100살 사이여야 합니다."),
    WAKEUPTIME_LENGTH_INVALID(BAD_REQUEST, "기상 시간은 ~30자 이내여야 합니다."),
    CLEANUPSTATUS_LENGTH_INVALID(BAD_REQUEST, "정리정돈 상태는 ~30자 이내여야 합니다."),
    SHOWERTIME_LENGTH_INVALID(BAD_REQUEST, "샤워시간은 ~30자 이내여야 합니다."),
    OPENKAKAOLINK_LENGTH_INVALID(BAD_REQUEST, "오픈채팅 링크는 ~100자 이내여야 합니다."),
    MBTI_LENGTH_INVALID(BAD_REQUEST, "mbti는 ~4자 이내여야 합니다."),
    MBTI_CHARACTER_INVALID(BAD_REQUEST, "올바른 mbti 형식이 아닙니다."),
    WISHTEXT_LENGTH_INVALID(BAD_REQUEST, "하고싶은 말은 ~150자 이내여야 합니다."),
    CONTENT_LENGTH_INVALID(BAD_REQUEST, "게시글 혹은 댓글 내용은 1~50자 이내여야 합니다."),
    TITLE_LENGTH_INVALID(BAD_REQUEST, "게시글 제목은 1~30자 이내여야 합니다."),

    ILLEGAL_ARGUMENT_ADMIN(BAD_REQUEST, "관리자는 해당 요청의 설정 대상이 될 수 없습니다."),
    ILLEGAL_ARGUMENT_SELF(BAD_REQUEST, "본인은 해당 요청의 설정 대상이 될 수 없습니다."),
    ILLEGAL_ARGUMENT_SLEEPOVERCALENDAR(BAD_REQUEST, "외박 일정은 해당 요청의 설정 대상이 될 수 없습니다."),
    ILLEGAL_ARGUMENT_FCM_TOKEN(BAD_REQUEST, "유효하지 않은 FCM 토큰 입니다."),
    ILLEGAL_ARGUMENT_DATE_SET(BAD_REQUEST, "시작일자가 종료일자보다 빠르거나 같아야 합니다."),

    ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC(BAD_REQUEST, "비공개 상태의 온보딩 정보 입니다."),
    ILLEGAL_STATEMENT_EXPLODEDTEAM(CONFLICT, "폭발한 팀은 요청 대상이 될 수 없습니다."),

    /**
     * 401 UNAUTHORIZED
     */
    INVALID_CODE(UNAUTHORIZED, "올바르지 않은 코드 입니다."),
    EXPIRED_CODE(UNAUTHORIZED, "이메일 인증 유효시간이 초과되었습니다."),

    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "유효하지 않은 토큰 입니다."),
    UNAUTHORIZED_PASSWORD(UNAUTHORIZED, "올바르지 않은 비밀번호 입니다."),
    UNAUTHORIZED_EMAIL(UNAUTHORIZED, "인증이 되지 않은 이메일 입니다."),
    UNAUTHORIZED_DELETED_MEMBER(UNAUTHORIZED, "탈퇴한 회원입니다. 로그아웃이 필요합니다."),

    /**
     * 403 FORBIDDEN
     */
    ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),
    ACCESS_DENIED_POST(FORBIDDEN, "게시글 업데이트 권한이 없습니다"),
    ACCESS_DENIED_COMMENT(FORBIDDEN, "댓글 업데이트 권한이 없습니다"),
    ACCESS_DENIED_TEAM(FORBIDDEN, "팀 접근 권한이 없습니다."),
    ACCESS_DENIED_TEAM_CALENDAR(FORBIDDEN, "팀 일정 접근 권한이 없습니다."),
    ACCESS_DENIED_SLEEPOVER_CALENDAR(FORBIDDEN, "팀 외박 일정 접근 권한이 없습니다."),

    /**
     * 404 NOT_FOUND
     */
    EMAIL_NOT_FOUND(NOT_FOUND, "등록된 이메일이 없습니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "등록된 멤버가 없습니다."),
    DISLIKEDMEMBER_NOT_FOUND(NOT_FOUND, "등록된 싫어요한 멤버가 없습니다."),
    LIKEDMEMBER_NOT_FOUND(NOT_FOUND, "등록된 좋아요한 멤버가 없습니다."),
    FILE_NOT_FOUND(NOT_FOUND, "등록된 사진이 없습니다."),
    MATCHINGINFO_NOT_FOUND(NOT_FOUND, "등록된 온보딩 정보가 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "등록된 댓글이 없습니다."),
    POSTLIKEDMEMBER_NOT_FOUND(NOT_FOUND, "공감하지 않은 게시글 입니다."),
    POST_NOT_FOUND(NOT_FOUND, "등록된 게시글이 없습니다."),
    POSTPHOTO_NOT_FOUND(NOT_FOUND, "등록된 게시글 사진이 없습니다."),
    MEMBERPHOTO_NOT_FOUND(NOT_FOUND, "등록된 프로필 사진이 없습니다."),
    CALENDAR_NOT_FOUND(NOT_FOUND, "등록된 공식 일정이 없습니다."),
    TEAM_NOT_FOUND(NOT_FOUND, "등록된 팀이 없습니다."),
    TEAMMEMBER_NOT_FOUND(NOT_FOUND, "등록되지 않은 팀 회원이 있습니다."),
    TEAMCALENDAR_NOT_FOUND(NOT_FOUND, "등록된 팀 일정이 없습니다."),

    DELETED_POST(NOT_FOUND, "삭제된 게시글 입니다."),
    DELETED_COMMENT(NOT_FOUND, "삭제된 댓글 입니다."),

    /**
     * 405 METHOD_NOT_ALLOWED
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 메서드입니다."),

    /**
     * 409 CONFLICT
     */
    DUPLICATE_EMAIL(CONFLICT, "등록된 이메일 입니다."),
    DUPLICATE_MEMBER(CONFLICT, "등록된 멤버 입니다."),
    DUPLICATE_NICKNAME(CONFLICT, "등록된 닉네임 입니다."),
    DUPLICATE_SAME_NICKNAME(CONFLICT, "기존의 닉네임과 동일합니다."),
    DUPLICATE_MATCHINGINFO(CONFLICT, "등록된 온보딩 정보가 존재합니다."),
    DUPLICATE_LIKED_MEMBER(CONFLICT, "좋아요한 멤버 입니다."),
    DUPLICATE_DISLIKED_MEMBER(CONFLICT, "싫어요한 멤버 입니다."),
    DUPLICATE_LIKED(CONFLICT, "공감한 게시글 입니다."),
    DUPLICATE_TEAM(CONFLICT, "등록된 팀이 존재합니다."),
    DUPLICATE_SLEEPOVER_DATE(CONFLICT, "중복인 외박 일자가 존재합니다."),

    CANNOT_UPDATE_NICKNAME(CONFLICT, "닉네임은 30일 간격으로 변경 가능합니다."),
    CANNOT_LIKED_SELF(CONFLICT, "본인 게시글은 공감할 수 없습니다."),
    CANNOT_LIKED_POST_BY_DELETED_MEMBER(CONFLICT, "탈퇴한 멤버의 게시글은 공감할 수 없습니다."),
    CANNOT_EXPLODE_TEAM(CONFLICT, "다른 팀원 존재 시 팀을 삭제할 수 없습니다."),
    CANNOT_REGISTER_TEAM_STATUS_FULL(CONFLICT, "등록하려는 팀이 정원 초과 입니다."),

    /**
     * 413 PAYLOAD_TOO_LARGE
     */
    FILE_SIZE_EXCEED(PAYLOAD_TOO_LARGE, "파일 용량이 초과되었습니다."),
    FILE_COUNT_EXCEED(PAYLOAD_TOO_LARGE, "파일 개수가 초과되었습니다."),

    /**
     * 415 UNSUPPORTED_MEDIA_TYPE
     */
    FILE_TYPE_UNSUPPORTED(UNSUPPORTED_MEDIA_TYPE, "파일 형식은 '.jpg', '.jpeg', '.png' 만 가능합니다."),

    /**
     * 500 SERVER_ERROR : 서버 에러
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다."),
    EMAIL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "이메일 전송 중 서버 에러가 발생했습니다."),
    FIREBASE_SERER_ERROR(INTERNAL_SERVER_ERROR, "푸시알림 처리 중 서버 에러가 발생했습니다."),
    S3_SERVER_ERROR(INTERNAL_SERVER_ERROR, "S3 처리 중 서버 에러가 발생했습니다."),
    CRAWLING_SERVER_ERROR(INTERNAL_SERVER_ERROR, "크롤링 중 서버 에러가 발생했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
