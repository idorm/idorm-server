package idorm.idormServer.dto;

import lombok.Data;

@Data
public class EmailResponseDTO {

    private String response;
    public EmailResponseDTO(String email){
        this.response =email;
    }
}