package idorm.idormServer.aop;

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

import static idorm.idormServer.exception.ExceptionCode.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Aspect
@Component
public class AopLogging {

    @Pointcut("execution(* idorm.idormServer..*Controller.*(..))")
    private void methodFromController(){}

    @Pointcut("execution(public * idorm.idormServer..*(..))")
    private void all() {
    }

    @Before("all()")
    public void logBeforeStart(JoinPoint joinPoint) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            String className = signature.getDeclaringType().getSimpleName();
            Method method = signature.getMethod();

            String[] parameterNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            String returnArgs = null;

            for (int i = 0; i < method.getParameters().length; i++) {
                if (parameterNames[i] != null) {
                    returnArgs += " | " + parameterNames[i] + " = ";
                    returnArgs += args[i] + "  ";
                }
            }
            log.info("[START] {} | {} {}", className, method.getName(), returnArgs);
        } catch (Exception e) {
            throw new CustomException(e, AOP_LOGGING_ERROR);
        }
    }

    @AfterReturning(value = "all()", returning = "result")
    public void logAfterSuccess(JoinPoint joinPoint, Object result) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            String className = signature.getDeclaringType().getSimpleName();
            Method method = signature.getMethod();

            log.info("[SUCCESS] {} | {} | return = {}", className, method.getName(), result);
        } catch (Exception e) {
            throw new CustomException(e, AOP_LOGGING_ERROR);
        }
    }

    @Around("methodFromController()")
    public Object logEndpoint(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String className = signature.getDeclaringType().getSimpleName();
        Method method = signature.getMethod();

        log.info("========= [API START] {} {} API =========", className, method.getName());
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        log.info("========= [API FINISH] {} {} API | {} ms =========", className, method.getName(), totalTimeMillis);
        return proceed;
    }

    @AfterThrowing(value = "all()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, CustomException exception) {

        try {

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            String className = signature.getDeclaringType().getSimpleName();
            Method method = signature.getMethod();

            String[] parameterNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            String returnArgs = null;

            for (int i = 0; i < method.getParameters().length; i++) {
                if (parameterNames[i] != null) {
                    returnArgs += " | " + parameterNames[i] + " = ";
                    returnArgs += args[i] + "  ";
                }
            }

            log.error("[ERROR] {} | {} | throwing = {} | reqArgs = {}",
                    className,
                    method.getName(),
                    exception.getExceptionCode().name(),
                    returnArgs);

            if (exception.getExceptionCode().getHttpStatus().equals(INTERNAL_SERVER_ERROR)) {

                if (exception.getException() != null) {
                    Sentry.captureException(exception);
                    log.error("##### SERVER ERROR DESCRIPTION #####", exception.getException());
                }
            }
        } catch (Exception e) {
            throw new CustomException(e, AOP_LOGGING_ERROR);
        }
    }
}
