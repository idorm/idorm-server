package idorm.idormServer.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static idorm.idormServer.exception.ExceptionCode.FORBIDDEN_AUTHORIZATION;

@Component("accessDeniedHandler")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    public CustomAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    private static DefaultExceptionResponseDto EXCEPTION_RESPONSE =
            new DefaultExceptionResponseDto(
                    FORBIDDEN_AUTHORIZATION.name(),
                    FORBIDDEN_AUTHORIZATION.getMessage()
            );

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(403);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try (OutputStream os = response.getOutputStream()) {
            mapper.writeValue(os, EXCEPTION_RESPONSE);
            os.flush();
        }
    }
}