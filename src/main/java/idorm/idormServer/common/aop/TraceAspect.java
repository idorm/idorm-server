package idorm.idormServer.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class TraceAspect {

    @Pointcut("execution(* idorm.idormServer..*(..))")
    private void all(){}

    @Pointcut("execution(* idorm.idormServer..*Controller.*(..))")
    private void apiTimer(){}

    @Around("apiTimer()")
    public Object AssumeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        final Class className = signature.getDeclaringType();
        final Method method = signature.getMethod();

        log.info("========= [START] {} {} API =========", className.getSimpleName(), method.getName());
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        log.info("========= [FINISH] {} {} API - {} ms =========", className.getSimpleName(), method.getName(), totalTimeMillis);
        return proceed;
    }

    @Before("all()")
    public void beforeLog(JoinPoint joinPoint) throws NoSuchMethodException {

        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        final Class className = signature.getDeclaringType();
        final Method method = signature.getMethod();

        final String[] parameterNames = signature.getParameterNames();
        final Object[] args = joinPoint.getArgs();

        String returnArgs = null;

        try {
            for(int i = 0; i < method.getParameters().length; i++) {
                returnArgs += " | " + parameterNames[i] + " = ";
                returnArgs += args[i] + "  ";
            }
        } catch (NullPointerException e) { }

        log.info("[START] {} | {} {}", className.getSimpleName(), method.getName(), returnArgs);
    }

    @AfterReturning(value = "all()", returning="result")
    public void afterReturnLog(JoinPoint joinPoint, Object result) throws NoSuchMethodException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Class className = signature.getDeclaringType();
        Method method = signature.getMethod();

        log.info("[RETURN] {} | {} | return = {}", className.getSimpleName(), method.getName(), result);
    }
}
