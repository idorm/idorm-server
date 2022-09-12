package idorm.idormServer.exceptions.http;

public class NotAcceptableException extends RuntimeException {

    public NotAcceptableException() {
        super();
    }

    public NotAcceptableException(String message) {
        super(message);
    }

    public NotAcceptableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAcceptableException(Throwable cause) {
        super(cause);
    }
}