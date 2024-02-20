package idorm.idormServer.common.aop;

import idorm.idormServer.common.exception.CustomException;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Aspect
@Component
public class LoggingAdvice {

    // TODO: Pointcut 범위 재설정
    @Pointcut("execution(* idorm.idormServer.calendar..*Controller.*(..)) || " +
            "execution(* idorm.idormServer.calendar..*Service.*(..)) || " +
            "execution(* idorm.idormServer.calendar..*Repository.*(..)) || " +
            "execution(* idorm.idormServer.community..*Controller.*(..)) || " +
            "execution(* idorm.idormServer.community..*Service.*(..)) || " +
            "execution(* idorm.idormServer.community..*Repository.*(..)) || " +
            "execution(* idorm.idormServer.notification..*Controller.*(..)) || " +
            "execution(* idorm.idormServer.notification..*Service.*(..)) || " +
            "execution(* idorm.idormServer.notification..*Repository.*(..)) || " +
            "execution(* idorm.idormServer.matchingInfo..*Controller.*(..)) || " +
            "execution(* idorm.idormServer.matchingInfo..*Service.*(..)) || " +
            "execution(* idorm.idormServer.matchingInfo..*Repository.*(..)) || " +
            "execution(* idorm.idormServer.matchingInfo..*Controller.*(..)) || " +
            "execution(* idorm.idormServer.matchingInfo..*Service.*(..)) || " +
            "execution(* idorm.idormServer.matchingInfo..*Repository.*(..)) || " +
            "execution(* idorm.idormServer.member..*Controller.*(..)) || " +
            "execution(* idorm.idormServer.member..*Service.*(..)) || " +
            "execution(* idorm.idormServer.member..*Repository.*(..)) || " +
            "execution(* idorm.idormServer.photo..*Controller.*(..)) || " +
            "execution(* idorm.idormServer.photo..*Service.*(..)) || " +
            "execution(* idorm.idormServer.photo..*Repository.*(..)) || " +
            "execution(* idorm.idormServer.report..*Repository.*(..)) || " +
            "execution(* idorm.idormServer.report..*Service.*(..)) || " +
            "execution(* idorm.idormServer.report..*Controller.*(..))")
    private void allFromBusinessLogic() {
    }

    @AfterThrowing(value = "allFromBusinessLogic()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, CustomException exception) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String className = signature.getDeclaringType().getSimpleName();
        Method method = signature.getMethod();

        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        StringBuilder returnArgs = new StringBuilder();

        for (int i = 0; i < method.getParameters().length; i++) {
            if (parameterNames[i] != null) {
                if (!parameterNames[i].equals("servletRequest")) {
                    returnArgs.append(" | ").append(parameterNames[i]).append(" = ");
                    returnArgs.append(args[i]).append(" ");
                }
            }
        }

        log.error("[ERROR] {} | {} | throwing = {} | reqArgs : {}",
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
    }
}
