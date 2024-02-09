package idorm.idormServer.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.exception.ExceptionCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static idorm.idormServer.common.exception.ExceptionCode.MEMBER_NOT_FOUND;
import static idorm.idormServer.common.exception.ExceptionCode.UNAUTHORIZED_MEMBER;

@Slf4j
@Component("JwtAuthenticationFilter")
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public CustomJwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = jwtTokenProvider.resolveToken(request);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (IllegalArgumentException e) {
            this.setExceptionResponse(response, UNAUTHORIZED_MEMBER);
        } catch (UsernameNotFoundException e) {
            this.setExceptionResponse(response, MEMBER_NOT_FOUND);
        }

        filterChain.doFilter(request, response);
    }

    private void setExceptionResponse(
            HttpServletResponse response,
            ExceptionCode exceptionCode
    ){
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(exceptionCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ErrorResponse errorResponse = new ErrorResponse(exceptionCode.toString(), exceptionCode.getMessage());
        try {
            OutputStream os = response.getOutputStream();
            mapper.writeValue(os, errorResponse);
            os.flush();
        } catch (IOException e) {
            log.error("[THROWING] CustomJwtAuthenticationFilter | setExceptionResponse | throwing = {}", e.getMessage());
        }
    }

    @Data
    private static class ErrorResponse{
        private final String responseCode;
        private final String responseMessage;
    }
}