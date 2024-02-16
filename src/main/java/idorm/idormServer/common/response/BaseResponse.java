package idorm.idormServer.common.response;

import idorm.idormServer.common.exception.GlobalResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class BaseResponse {

    private final Boolean isSuccess;
    private final String responseCode;
    private final String responseMessage;

    public static BaseResponse of(Boolean isSuccess, GlobalResponseCode globalResponseCode){
        return new BaseResponse(isSuccess, globalResponseCode.name(), globalResponseCode.getMessage());
    }

    public static BaseResponse of(boolean isSuccess, GlobalResponseCode globalResponseCode, String message){
        return new BaseResponse(isSuccess, globalResponseCode.name(), message);
    }

    public static BaseResponse success(){
        return new BaseResponse(true, "200", "호출에 성공하셨습니다.");
    }
}
