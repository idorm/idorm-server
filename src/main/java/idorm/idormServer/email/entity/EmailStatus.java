package idorm.idormServer.email.entity;

public enum EmailStatus {
	SEND,
	VERIFIED,
	RE_SEND,
	RE_VERIFIED,
	DELETED;

	public boolean isNotVerified() {
		return !this.equals(VERIFIED);
	}

	public boolean isNotReverified() {
		return !this.equals(RE_VERIFIED);
	}
}
