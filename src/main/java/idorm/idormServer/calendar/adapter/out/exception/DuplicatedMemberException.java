package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class DuplicatedMemberException extends BaseException {

	public DuplicatedMemberException() {
		super(CalendarResponseCode.DUPLICATED_MEMBER);
	}
}