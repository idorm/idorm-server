package idorm.idormServer.aspect;

import idorm.idormServer.exception.CustomException;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Aspect
@Component
public class AopLogging {

    @Pointcut("execution(* idorm.idormServer..*Controller.*(..))")
    private void apiTimer(){}

    @Pointcut("execution(public * idorm.idormServer..*Service..*(..))")
    private void methodFromService() {
    }

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

    @AfterThrowing(value = "methodFromService()", throwing = "exception")
    public void logAfterThrowingFromService(JoinPoint joinPoint, CustomException exception) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Class className = signature.getDeclaringType();
            Method method = signature.getMethod();

            final String[] parameterNames = signature.getParameterNames();
            final Object[] args = joinPoint.getArgs();

            String returnArgs = null;

            for (int i = 0; i < method.getParameters().length; i++) {
                if (parameterNames[i] != null) {
                    returnArgs += " | " + parameterNames[i] + " = ";
                    returnArgs += args[i] + "  ";
                }
            }

            log.error("[ERROR] {} | {} | throwing = {} | reqArgs = {}",
                    className.getSimpleName(),
                    method.getName(),
                    exception.getExceptionCode().name(),
                    returnArgs);

            if (exception.getExceptionCode().getHttpStatus().equals(INTERNAL_SERVER_ERROR)) {
                if (exception.getException() != null) {
                    Sentry.captureException(exception);
                    log.error("***** SERVER ERROR DESCRIPTION *****", exception.getException());
                }
            }
        } catch (Exception e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
}
