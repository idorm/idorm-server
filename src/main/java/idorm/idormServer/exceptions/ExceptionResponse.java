package idorm.idormServer.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private int code;
    private String message;
    private String details;

}