package idorm.idormServer.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static idorm.idormServer.exception.ExceptionCode.FORBIDDEN_AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static DefaultExceptionResponseDto errorResponse =
            new DefaultExceptionResponseDto(
                    FORBIDDEN_AUTHORIZATION.name(),
                    FORBIDDEN_AUTHORIZATION.getMessage());

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException exception) throws IOException {

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(FORBIDDEN.value());
        try (OutputStream os = httpServletResponse.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, errorResponse);
            os.flush();
        }
    }
}