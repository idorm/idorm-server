package idorm.idormServer.common.exception;

import com.google.common.annotations.VisibleForTesting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException{
    private final BaseResponseCode code;

    @VisibleForTesting
    public String getMessage() {
        return code.getMessage();
    }
}
