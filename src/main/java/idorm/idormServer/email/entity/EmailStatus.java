package idorm.idormServer.email.entity;

public enum EmailStatus {
    SEND,
    VERIFIED,
    RE_SEND,
    RE_VERIFIED;

    public boolean isNot(EmailStatus emailStatus) {
        return !this.equals(emailStatus);
    }
}
