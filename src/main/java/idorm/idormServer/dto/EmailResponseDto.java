package idorm.idormServer.dto;

import lombok.Data;

@Data
public class EmailResponseDto {

    private String response;
    public EmailResponseDto(String email){
        this.response =email;
    }
}