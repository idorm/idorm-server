package idorm.idormServer.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sentry.Sentry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static idorm.idormServer.common.exception.ExceptionCode.METHOD_NOT_ALLOWED;

@Slf4j
@Aspect
@Component
public class FilterChainProxyAspect {

    @Around("execution(public void org.springframework.security.web.FilterChainProxy.doFilter(..))")
    public void handleRequestRejectedException (ProceedingJoinPoint pjp) throws Throwable {
        try {
            pjp.proceed();
        } catch (RequestRejectedException e) {
            HttpServletResponse response = (HttpServletResponse) pjp.getArgs()[1];
            log.error("[THROWING] FilterChainProxyAspect | handleRequestRejectedException | throwing = RequestRejectedException");
            setExceptionResponse(response);
        }
    }

    private void setExceptionResponse(HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(METHOD_NOT_ALLOWED.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ExceptionResponse errorResponse = new ExceptionResponse(METHOD_NOT_ALLOWED.toString(),
                METHOD_NOT_ALLOWED.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            Sentry.captureException(e);
            log.error("[THROWING] ExceptionHandlerFilter | setExceptionResponse | throwing = {}", e.getMessage());
        }
    }

    @Data
    private static class ExceptionResponse{
        private final String responseCode;
        private final String responseMessage;
    }
}