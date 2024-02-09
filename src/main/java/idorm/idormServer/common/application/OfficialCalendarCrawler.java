package idorm.idormServer.common.application;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import idorm.idormServer.calendar.dto.CrawledOfficialCalendarResponse;
import idorm.idormServer.calendar.service.OfficialCalendarService;
import idorm.idormServer.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static idorm.idormServer.common.exception.ExceptionCode.CRAWLING_SERVER_ERROR;

@Component
@RequiredArgsConstructor
public class OfficialCalendarCrawler {

    private final OfficialCalendarService officialCalendarService;

    public List<CrawledOfficialCalendarResponse> crawlPosts() {

        List<CrawledOfficialCalendarResponse> postList = new ArrayList<>();

        String url = "https://bioeng.inu.ac.kr/dorm/6528/subview.do";

        try{
            Document doc = Jsoup.connect(url).get();
            Elements postElements = doc.select("table.board-table tbody tr");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            LocalDate today = LocalDate.now();

            int cnt = 0;

            for (Element postElement : postElements) {

                if (cnt >= 10)
                    break;

                LocalDate date = LocalDate.parse(postElement.select("td.td-date").text(), formatter);

                if (date.isBefore(today.minusDays(7)))
                    continue;

                String postUrl = "https://bioeng.inu.ac.kr/" + postElement.select("td.td-subject a").attr("href");
                String postId = parseInuPostId(postUrl);

                if (officialCalendarService.validateOfficialCalendarExistence(postId))
                    continue;

                String postTitle = postElement.select("td.td-subject a strong").text();

                OfficialCalendar createdCalendar = officialCalendarService.save(postId, postTitle, date, postUrl);

                CrawledOfficialCalendarResponse calendarResponse = new CrawledOfficialCalendarResponse(createdCalendar);
                postList.add(calendarResponse);

                cnt++;
            }

            return postList;
        } catch (IOException e) {
            throw new CustomException(e, CRAWLING_SERVER_ERROR);
        }
    }

    private String parseInuPostId(String url) {

        return url
                .replace("https://bioeng.inu.ac.kr//bbs/dorm/", "")
                .replace("/artclView.do", "")
                .split("/")[1];
    }
}
