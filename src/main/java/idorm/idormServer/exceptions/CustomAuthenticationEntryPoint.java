package idorm.idormServer.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static idorm.idormServer.exceptions.ErrorCode.UNAUTHORIZED_MEMBER;
import static org.springframework.http.HttpStatus.*;

@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static ErrorResponse errorResponse =
            new ErrorResponse(UNAUTHORIZED.name(),
                    UNAUTHORIZED_MEMBER.name(),
                    UNAUTHORIZED_MEMBER.getDetail());

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException ex) throws IOException, ServletException {

        // [commence] 인증 실패로 response.sendError 발생
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try (OutputStream os = response.getOutputStream()) {
            objectMapper.writeValue(os, errorResponse);
            os.flush();
        }
    }
}