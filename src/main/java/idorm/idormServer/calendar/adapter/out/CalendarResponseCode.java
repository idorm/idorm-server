package idorm.idormServer.calendar.adapter.out;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CalendarResponseCode implements BaseResponseCode {

  //성공
  CALENDAR_UPDATED(OK, "공식 일정 수정 완료"),
  OFFICIAL_CALENDAR_DELETED(OK, "공식 일정 삭제 완료"),
  OFFICIAL_CALENDARS_FOUND(OK, "관리자용 월별 공식 일정 조회 완료"),
  CALENDAR_FOUND(OK, "관리자용 공식 일정 단건 조회 완료"),
  CALENDAR_MANY_FOUND(OK, "월별 공식 일정 다건 조회"),
  SLEEPOVER_CALENDAR_UPDATED(OK, "외박 일정 수정 완료"),
  TEAM_CALENDER_UPDATED(OK, "팀 일정 수정 완료"),
  TEAM_CALENDER_DELETED(OK, "팀 일정 삭제 완료"),
  TEAM_CALENDER_FOUND(OK, "팀 일정 단건 조회 완료"),
  TEAM_CALENDERS_FOUND(OK, "팀 일정 월별 조회 완료"),
  TEAM_SLEEPOVER_CALENDERS_FOUND(OK, "팀 외박 일정 월별 조회 완료"),
  TEAM_MEMBER_DELETED(OK, "팀 회원 삭제 완료"),
  TEAM_MEMBERS_FOUND(OK, "팀 회원 다건 조회 완료"),
  TEAM_EXPLODED_CHECKED(OK, "팀 삭제 완료"),

  TEAM_CALENDER_CREATED(CREATED, "팀 일정 생성 완료"),
  TEAM_MEMBER_CREATED(CREATED, "팀 회원 초대 완료"),
  SLEEPOVER_CALENDAR_CREATED(CREATED, "외박 일정 생성 완료"),

  //실패
  INVALID_TITLE_LENGTH(BAD_REQUEST, "일정 제목은 1~30자 이내여야 합니다."),
  INVALID_CONTENT_LENGTH(BAD_REQUEST, "일정 내용은 1~100자 이내여야 합니다."),
  ILLEGAL_STATEMENT_EXPLODEDTEAM(BAD_REQUEST, "폭발한 팀은 요청 대상이 될 수 없습니다."),
  ILLEGAL_ARGUMENT_SLEEPOVERCALENDAR(BAD_REQUEST, "외박 일정은 해당 요청의 설정 대상이 될 수 없습니다."),
  ILLEGAL_ARGUMENT_DATE_SET(BAD_REQUEST, "시작일자가 종료일자보다 빠르거나 같아야 합니다."),
  FIELD_TARGETS_REQUIRED(BAD_REQUEST, "팀 일정 대상자 입력은 필수 입니다."),
  FILED_DATE_REQUIRED(BAD_REQUEST, "날짜 입력은 필수입니다."),

  ACCESS_DENIED_TEAM(FORBIDDEN, "팀 접근 권한이 없습니다."),
  ACCESS_DENIED_TEAM_CALENDAR(FORBIDDEN, "팀 일정 접근 권한이 없습니다."),
  ACCESS_DENIED_SLEEPOVER_CALENDAR(FORBIDDEN, "팀 외박 일정 접근 권한이 없습니다."),
  NOT_FOUND_TEAM_CALENDAR(NOT_FOUND, "등록된 팀 일정이 없습니다."),
  NOT_FOUND_TEAM(NOT_FOUND, "등록된 팀이 없습니다."),
  NOT_FOUND_TEAM_MEMBER(NOT_FOUND, "등록되지 않은 팀 회원이 있습니다."),
  NOT_FOUND_CALENDAR(NOT_FOUND, "등록된 공식 일정이 없습니다."),
  NOT_FOUND_SLEEPOVER_CALENDAR(NOT_FOUND, "등록된 외박 일정이 없습니다."),


  DUPLICATED_MEMBER(CONFLICT, "등록된 멤버 입니다."),
  DUPLICATED_SLEEPOVER_DATE(CONFLICT, "중복인 외박 일자가 존재합니다."),
  ALREADY_REGISTERED_TEAM(CONFLICT, "이미 등록된 팀이 있습니다."),
  ALREADY_DELETED_MEMBER(CONFLICT, "이미 삭제된 멤버입니다."),
  CANNOT_EXPLODE_TEAM(CONFLICT, "다른 팀원 존재 시 팀을 삭제할 수 없습니다."),
  CANNOT_REGISTER_TEAM_STATUS_FULL(CONFLICT, "등록하려는 팀이 정원 초과 입니다.");

  private final HttpStatus status;
  private final String message;

  @Override
  public String getName() {
    return this.name();
  }
}