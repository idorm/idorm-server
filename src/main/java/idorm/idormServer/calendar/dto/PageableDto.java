package idorm.idormServer.calendar.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageableDto {
    @ApiModelProperty(dataType = "int")
    int size = 10;

    @ApiModelProperty(dataType = "int")
    int page = 0;

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
