package idorm.idormServer.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import idorm.idormServer.exception.ExceptionCode;
import io.sentry.Sentry;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {

        try {
            filterChain.doFilter(request, response);
        } catch (UsernameNotFoundException e) {
            setExceptionResponse(response, ExceptionCode.UNAUTHORIZED_DELETED_MEMBER);
        } catch (Exception e) {
            Sentry.captureException(e);
            log.error("[THROWING] ExceptionHandlerFilter | doFilterInternal | throwing = {}", e.getMessage());
            setExceptionResponse(response, SERVER_ERROR);
        }
    }

    private void setExceptionResponse(
            HttpServletResponse response,
            ExceptionCode exceptionCode
    ){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(exceptionCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(exceptionCode.toString(), exceptionCode.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            Sentry.captureException(e);
            log.error("[THROWING] ExceptionHandlerFilter | setExceptionResponse | throwing = {}", e.getMessage());
        }
    }

    @Data
    public static class ErrorResponse{
        private final String responseCode;
        private final String responseMessage;
    }
}
