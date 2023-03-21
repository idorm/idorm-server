package idorm.idormServer.aspect;

import idorm.idormServer.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(public * idorm.idormServer..*(..))" +
            "&& !execution(public * idorm.idormServer..*Repository.*(..))")
    private void allExceptRepository() {
    }

    @Pointcut("execution(public * idorm.idormServer..*Repository.find*(..))")
    private void readFromRepository() {
    }

    @Pointcut("execution(public * idorm.idormServer..*Repository.save(..))")
    private void saveFromRepository() {
    }

    @Pointcut("execution(public * idorm.idormServer..*Repository.delete(..))")
    private void deleteFromRepository() {
    }

    @Pointcut("execution(* idorm.idormServer..*Controller.*(..))")
    private void apiTimer(){}

    @Around("apiTimer()")
    public Object AssumeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        final Class className = signature.getDeclaringType();
        final Method method = signature.getMethod();

        log.info("========= [API START] {} {} API =========", className.getSimpleName(), method.getName());
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        log.info("========= [API FINISH] {} {} API | {} ms =========",
                className.getSimpleName(),
                method.getName(),
                totalTimeMillis);
        return proceed;
    }

    @Before("allExceptRepository()")
    public void beforeLogExceptRepository(JoinPoint joinPoint) {

        try {
            final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            final Class className = signature.getDeclaringType();
            final Method method = signature.getMethod();

            final String[] parameterNames = signature.getParameterNames();
            final Object[] args = joinPoint.getArgs();

            String returnArgs = null;

            for (int i = 0; i < method.getParameters().length; i++) {
                if (parameterNames[i] != null) {
                    returnArgs += " | " + parameterNames[i] + " = ";
                    returnArgs += args[i] + "  ";
                }
            }
            log.info("[START] {} | {} {}", className.getSimpleName(), method.getName(), returnArgs);
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @Before("readFromRepository()")
    public void beforeLogReadFromRepository(JoinPoint joinPoint) {

        try {
            final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            final Class className = signature.getDeclaringType();
            final Method method = signature.getMethod();

            final Parameter[] parameterNames = method.getParameters();
            final Object[] args = joinPoint.getArgs();

            String returnArgs = null;

            for (int i = 0; i < parameterNames.length; i++) {
                if (parameterNames[i] != null) {
                    returnArgs += " | " + parameterNames[i] + " = ";
                    returnArgs += args[i] + "  ";
                }
            }
            log.info("[START | REPOSITORY | READ] {} | {} {}", className.getSimpleName(), method.getName(), returnArgs);
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @Before("saveFromRepository()")
    public void beforeLogSaveFromRepository(JoinPoint joinPoint) {

        try {
            final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            final Class className = signature.getDeclaringType();
            final Method method = signature.getMethod();

            final String[] parameterNames = signature.getParameterNames();
            final Object[] args = joinPoint.getArgs();

            String returnArgs = null;

            for (int i = 0; i < method.getParameters().length; i++) {
                if (parameterNames[i] != null) {
                    returnArgs += " | " + parameterNames[i] + " = ";
                    returnArgs += args[i] + "  ";
                }
            }
            log.info("[START | REPOSITORY | SAVE] {} | {} {}", className.getSimpleName(), method.getName(), returnArgs);
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @Before("deleteFromRepository()")
    public void beforeLogDeleteFromRepository(JoinPoint joinPoint) {

        try {
            final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            final Class className = signature.getDeclaringType();
            final Method method = signature.getMethod();

            final String[] parameterNames = signature.getParameterNames();
            final Object[] args = joinPoint.getArgs();

            String returnArgs = null;

            for (int i = 0; i < method.getParameters().length; i++) {
                if (parameterNames[i] != null) {
                    returnArgs += " | " + parameterNames[i] + " = ";
                    returnArgs += args[i] + "  ";
                }
            }
            log.info("[START | REPOSITORY | DELETE] {} | {} {}", className.getSimpleName(), method.getName(), returnArgs);
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @AfterReturning(value = "allExceptRepository()", returning = "result")
    public void logAfterSuccessAllMethodsExceptRepository(JoinPoint joinPoint, Object result) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Class className = signature.getDeclaringType();
            Method method = signature.getMethod();

            log.info("[SUCCESS] {} | {} | return = {}", className.getSimpleName(), method.getName(), result);
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @AfterReturning(value = "readFromRepository()", returning = "result")
    public void logAfterSuccessReadFromRepository(JoinPoint joinPoint, Object result) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Class className = signature.getDeclaringType();
            Method method = signature.getMethod();

            log.info("[SUCCESS | REPOSITORY | READ] {} | {} | return = {}",
                    className.getSimpleName(),
                    method.getName(),
                    result);
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @AfterReturning(value = "saveFromRepository()", returning = "result")
    public void logAfterSuccessSaveFromRepository(JoinPoint joinPoint, Object result) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Class className = signature.getDeclaringType();
            Method method = signature.getMethod();

            log.info("[SUCCESS | REPOSITORY | SAVE] {} | {} | return = {}",
                    className.getSimpleName(),
                    method.getName(),
                    result);
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @AfterReturning(value = "deleteFromRepository()", returning = "result")
    public void logAfterSuccessDeleteFromRepository(JoinPoint joinPoint, Object result) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Class className = signature.getDeclaringType();
            Method method = signature.getMethod();

            log.info("[SUCCESS | REPOSITORY | DELETE] {} | {} | return = {}",
                    className.getSimpleName(),
                    method.getName(),
                    result);
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @AfterThrowing(value = "allExceptRepository()", throwing = "exception")
    public void logAfterThrowingAllMethodsExceptRepository(JoinPoint joinPoint, CustomException exception) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Class className = signature.getDeclaringType();
            Method method = signature.getMethod();

            log.error("[THROWING] {} | {} | throwing = {}",
                    className.getSimpleName(),
                    method.getName(),
                    exception.getExceptionCode().name());

            if (exception.getExceptionCode().getHttpStatus().equals(INTERNAL_SERVER_ERROR)) {
                if (exception.getException() != null)
                    log.error("***** SERVER ERROR DESCRIPTION *****", exception.getException());
            }
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @AfterThrowing(value = "readFromRepository()", throwing = "exception")
    public void logAfterThrowingReadFromRepository(JoinPoint joinPoint, CustomException exception) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Class className = signature.getDeclaringType();
            Method method = signature.getMethod();

            log.error("[THROWING | REPOSITORY | READ] {} | {} | throwing = {}",
                    className.getSimpleName(),
                    method.getName(),
                    exception.getExceptionCode().name());

            if (exception.getExceptionCode().getHttpStatus().equals(INTERNAL_SERVER_ERROR)) {
                if (exception.getException() != null)
                    log.error("***** SERVER ERROR DESCRIPTION *****", exception.getException());
            }
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @AfterThrowing(value = "saveFromRepository()", throwing = "exception")
    public void logAfterThrowingSaveFromRepository(JoinPoint joinPoint, CustomException exception) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Class className = signature.getDeclaringType();
            Method method = signature.getMethod();

            log.error("[THROWING | REPOSITORY | SAVE] {} | {} | throwing = {}",
                    className.getSimpleName(),
                    method.getName(),
                    exception.getExceptionCode().name());

            if (exception.getExceptionCode().getHttpStatus().equals(INTERNAL_SERVER_ERROR)) {
                if (exception.getException() != null)
                    log.error("***** SERVER ERROR DESCRIPTION *****", exception.getException());
            }
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @AfterThrowing(value = "deleteFromRepository()", throwing = "exception")
    public void logAfterThrowingDeleteFromRepository(JoinPoint joinPoint, CustomException exception) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Class className = signature.getDeclaringType();
            Method method = signature.getMethod();

            log.error("[THROWING | REPOSITORY | DELETE] {} | {} | throwing = {}",
                    className.getSimpleName(),
                    method.getName(),
                    exception.getExceptionCode().name());

            if (exception.getExceptionCode().getHttpStatus().equals(INTERNAL_SERVER_ERROR)) {
                if (exception.getException() != null)
                    log.error("***** SERVER ERROR DESCRIPTION *****", exception.getException());
            }
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
}
