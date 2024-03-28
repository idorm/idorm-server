package idorm.idormServer.common.logging;

import java.lang.reflect.Method;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import idorm.idormServer.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Profile("local")
@Slf4j
@Aspect
@Component
public class LoggingAdvice {

	@Pointcut("execution(* idorm.idormServer..*(..))")
	private void allPointcut() {
	}

	@Pointcut("@annotation(idorm.idormServer.common.logging.ExecutionTimer)")
	private void executionTimer() {
	}

	@Around("executionTimer()")
	public void logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		joinPoint.proceed();
		stopWatch.stop();

		long totalTimeMillis = stopWatch.getTotalTimeMillis();

		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		String methodName = signature.getMethod().getName();

		log.info("실행 메서드: {}, 실행시간 = {}ms", methodName, totalTimeMillis);
	}

	@Before("allPointcut()")
	public void preHandle(JoinPoint joinPoint) {
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

	@AfterReturning(value = "allPointcut()", returning = "result")
	public void afterHandle(JoinPoint joinPoint, Object result) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Class className = signature.getDeclaringType();
		Method method = signature.getMethod();

		log.info("[SUCCESS] {} | {} | return = {}", className.getSimpleName(), method.getName(), result);
	}

	@AfterThrowing(value = "allPointcut()", throwing = "exception")
	public void exceptionHandle(JoinPoint joinPoint, BaseException exception) {
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

		log.error("[ERROR] {} | {} | throwing = {} | reqArgs : {}", className, method.getName(),
			exception.getCode().getName(), returnArgs);
	}
}
