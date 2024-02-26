package idorm.idormServer.common.aop;

import static org.springframework.http.HttpStatus.*;

import java.lang.reflect.Method;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import idorm.idormServer.common.exception.BaseException;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAdvice {

	// TODO: Pointcut 범위 재설정
	// @Pointcut("execution(* idorm.idormServer.calendar..*Controller.*(..)) || " +
	//         "execution(* idorm.idormServer.calendar..*Service.*(..)) || " +
	//         "execution(* idorm.idormServer.calendar..*Repository.*(..)) || " +
	//         "execution(* idorm.idormServer.community..*Controller.*(..)) || " +
	//         "execution(* idorm.idormServer.community..*Service.*(..)) || " +
	//         "execution(* idorm.idormServer.community..*Repository.*(..)) || " +
	//         "execution(* idorm.idormServer.notification..*Controller.*(..)) || " +
	//         "execution(* idorm.idormServer.notification..*Service.*(..)) || " +
	//         "execution(* idorm.idormServer.notification..*Repository.*(..)) || " +
	//         "execution(* idorm.idormServer.matchingInfo..*Controller.*(..)) || " +
	//         "execution(* idorm.idormServer.matchingInfo..*Service.*(..)) || " +
	//         "execution(* idorm.idormServer.matchingInfo..*Repository.*(..)) || " +
	//         "execution(* idorm.idormServer.matchingInfo..*Controller.*(..)) || " +
	//         "execution(* idorm.idormServer.matchingInfo..*Service.*(..)) || " +
	//         "execution(* idorm.idormServer.matchingInfo..*Repository.*(..)) || " +
	//         "execution(* idorm.idormServer.member..*Controller.*(..)) || " +
	//         "execution(* idorm.idormServer.member..*Service.*(..)) || " +
	//         "execution(* idorm.idormServer.member..*Repository.*(..)) || " +
	//         "execution(* idorm.idormServer.photo..*Controller.*(..)) || " +
	//         "execution(* idorm.idormServer.photo..*Service.*(..)) || " +
	//         "execution(* idorm.idormServer.photo..*Repository.*(..)) || " +
	//         "execution(* idorm.idormServer.report..*Repository.*(..)) || " +
	//         "execution(* idorm.idormServer.report..*Service.*(..)) || " +
	//         "execution(* idorm.idormServer.report..*Controller.*(..))")
	@Pointcut("execution(* idorm.idormServer..*(..))")
	private void allFromBusinessLogic() {
	}

	@Before("allFromBusinessLogic()")
	public void beforeLogExceptRepository(JoinPoint joinPoint) {
		final MethodSignature signature = (MethodSignature)joinPoint.getSignature();

		final Class className = signature.getDeclaringType();
		final Method method = signature.getMethod();

		final String[] parameterNames = signature.getParameterNames();
		final Object[] args = joinPoint.getArgs();

		String returnArgs = null;

		if (!Objects.isNull(parameterNames)) {
			for (int i = 0; i < parameterNames.length; i++) {
				if (parameterNames[i] != null) {
					returnArgs += " | " + parameterNames[i] + " = ";
					returnArgs += args[i] + "  ";
				}
			}
		}
		log.info("[START] {} | {} {}", className.getSimpleName(), method.getName(), returnArgs);
	}

	@AfterReturning(value = "allFromBusinessLogic()", returning = "result")
	public void logAfterSuccessAllMethodsExceptRepository(JoinPoint joinPoint, Object result) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();

		Class className = signature.getDeclaringType();
		Method method = signature.getMethod();

		log.info("[SUCCESS] {} | {} | return = {}", className.getSimpleName(), method.getName(), result);
	}

	@AfterThrowing(value = "allFromBusinessLogic()", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, BaseException exception) {

		MethodSignature signature = (MethodSignature)joinPoint.getSignature();

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
			exception.getCode().getName(),
			returnArgs);

		if (exception.getCode().getStatus().equals(INTERNAL_SERVER_ERROR)) {

			if (exception.getCode() != null) {
				Sentry.captureException(exception);
				log.error("##### SERVER ERROR DESCRIPTION #####", exception.getCode());
			}
		}
	}
}
