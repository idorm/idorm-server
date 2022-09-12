//package idorm.idormServer.exceptions;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.OutputStream;
//
//@Component
//public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//
//    private static DefaultExceptionResponseDto exceptionResponse =
//            new DefaultExceptionResponseDto("FORBIDDEN", null);
//
//    @Override
//    public void handle(HttpServletRequest httpServletRequest,
//                       HttpServletResponse httpServletResponse,
//                       AccessDeniedException e) throws IOException, ServletException {
//
//
//        //response에 넣기
//        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
//        try (OutputStream os = httpServletResponse.getOutputStream()) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.writeValue(os, exceptionResponse);
//            os.flush();
//        }
//    }
//}