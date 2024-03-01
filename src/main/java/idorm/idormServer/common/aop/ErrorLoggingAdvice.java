package idorm.idormServer.common.aop;

import static org.springframework.http.HttpStatus.*;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import idorm.idormServer.common.exception.BaseException;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;

@Profile("dev")
@Slf4j
@Aspect
@Component
public class ErrorLoggingAdvice {

	@Pointcut("execution(* idorm.idormServer..*(..))")
	private void all() {
	}

	@AfterThrowing(value = "all()", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, BaseException exception) {
		if (!exception.getCode().getStatus().equals(INTERNAL_SERVER_ERROR)) {
			return;
		}

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

		Sentry.captureException(exception);
		log.error("[SERVER ERROR] {} | {} | throwing = {} | reqArgs : {}", className, method.getName(),
			exception.getCode().getName(), returnArgs);
		log.error("[SERVER ERROR DESCRIPTION] code : {} | message : {}", exception.getCode(), exception.getMessage());
	}
}
