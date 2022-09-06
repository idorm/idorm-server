package idorm.idormServer.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NonUniqueResultException;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExceptionsHandler {

    /**
     * 비어있는, 없는 공간의 값을 꺼내려고 할 때 발생
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResult> noSuch(NoSuchElementException e){
        ErrorResult errorResult = new ErrorResult("NoSuch-Element", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * 적절하지 않은 인자를 메소드에 넘겨주었을 때 발생
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> illegalArgument(IllegalArgumentException e){
        ErrorResult errorResult = new ErrorResult("Illegal-Argument",e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * null을 만났을 때 발생
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResult> nullPointer(NullPointerException e){
        ErrorResult errorResult = new ErrorResult("Null-Point",e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * 권한 없는 사용자가 접근 시 발생
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResult> accessDenied(AccessDeniedException e){
        ErrorResult errorResult = new ErrorResult("Forbidden",e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.FORBIDDEN);
    }

    /**
     * 단건 조회 시 결과가 2건 이상 일 때 발생
     */
    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<ErrorResult> accessDenied(NonUniqueResultException e){
        ErrorResult errorResult = new ErrorResult("Non-Unique",e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * @valid  유효성 체크에 통과하지 못할 때 발생한다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> methodValidException(MethodArgumentNotValidException e){

        String errorMessage = e.getFieldError().getDefaultMessage();
        ErrorResult errorResult = new ErrorResult("Not-Valid", errorMessage);
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

}