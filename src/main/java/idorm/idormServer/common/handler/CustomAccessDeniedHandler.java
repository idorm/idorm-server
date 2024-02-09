package idorm.idormServer.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import idorm.idormServer.common.dto.DefaultExceptionResponseDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static idorm.idormServer.common.exception.ExceptionCode.ACCESS_DENIED;

@Component("accessDeniedHandler")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    public CustomAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    private static final DefaultExceptionResponseDto EXCEPTION_RESPONSE =
            new DefaultExceptionResponseDto(
                    ACCESS_DENIED.name(),
                    ACCESS_DENIED.getMessage()
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