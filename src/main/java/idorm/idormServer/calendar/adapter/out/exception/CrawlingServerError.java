package idorm.idormServer.calendar.adapter.out.exception;

import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.*;

import idorm.idormServer.common.exception.BaseException;

public class CrawlingServerError extends BaseException {
	public CrawlingServerError() {
		super(CRAWLING_SERVER_ERROR);
	}
}
