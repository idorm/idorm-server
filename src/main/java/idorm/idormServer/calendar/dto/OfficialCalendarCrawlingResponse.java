package idorm.idormServer.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfficialCalendarCrawlingResponse {

    private final String title;
//    private final String content;
    private final String websiteUrl;
    private final String file;
}
