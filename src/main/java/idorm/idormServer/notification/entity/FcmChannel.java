package idorm.idormServer.notification.entity;

public enum FcmChannel {
	CALENDAR("오늘의 공식 일정 알림입니다."),
	TEAMCALENDAR("오늘의 룸메이트 일정 알림입니다."),
	COMMENT("새로운 댓글 알림입니다."),
	SUBCOMMENT("새로운 대댓글 알림입니다."),
	TOPPOST("어제 인기 게시글 알림입니다.");

	private String title;

	FcmChannel(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}