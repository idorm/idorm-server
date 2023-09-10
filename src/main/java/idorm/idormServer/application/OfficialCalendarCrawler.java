package idorm.idormServer.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import idorm.idormServer.calendar.domain.CrawledOfficialCalendar;
import idorm.idormServer.calendar.dto.OfficialCalendarCrawlingResponse;
import idorm.idormServer.calendar.service.CrawledOfficialCalendarService;
import idorm.idormServer.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static idorm.idormServer.exception.ExceptionCode.CRAWLING_SERVER_ERROR;

@Component
@RequiredArgsConstructor
public class OfficialCalendarCrawler {

    @Autowired
    public ObjectMapper mapper;

    private final CrawledOfficialCalendarService officialCrawledCalendarService;

    @Transactional
    public int crawling() {

        try {
            List<String> urls = parseBoardList(1202839, 447638, 1);

            List<CrawledOfficialCalendar> createdCalendars = new ArrayList<>();

            int newCalendarCount = 0;

            for (String url : urls) {

                OfficialCalendarCrawlingResponse result = parseBoardDetail(1202839,
                        447638,
                        1,
                        0,
                        url);
                String websiteUrl = result.getWebsiteUrl();
                String inuPostId = "-999";

                if (websiteUrl != null){
                    int startIdx = websiteUrl.indexOf("&boardSeq=", 40) + 10;
                    int endIdx = websiteUrl.indexOf("&", startIdx + 1);

                    inuPostId = websiteUrl.substring(startIdx, endIdx);
                }

                //TODO: 30일마다 크롤링 게시글 정리 / 존재 여부는 공식 일정 DB, 크롤링 DB에서 inuPostId 로 체크
                if (officialCrawledCalendarService.validateCrawledCalendarExistence(inuPostId))
                    continue;

                String title = result.getTitle().replace("제목 : ", "");
//                String content = result.getContent();
                String file = result.getFile();

                officialCrawledCalendarService.save(title, websiteUrl, inuPostId);
                // TODO: OfficialCalendarPhoto 저장

                newCalendarCount++;
            }

            // TODO: 관리자에게 알림을 보낸다. 00개의 새로운 공식 일정이 있습니다.
            return newCalendarCount;
        } catch (IOException e) {
            throw new CustomException(e, CRAWLING_SERVER_ERROR);
        }
    }

    private static List<String> parseBoardList(int codyMenuSeq, int boardId, int page) throws IOException {

        String url = String.format("https://www.inu.ac.kr/user/indexSub.do?codyMenuSeq=%d&siteId=dorm&dum=dum&boardId=%d&page=%d&command=list&boardSeq=", codyMenuSeq, boardId, page);
        Document doc = Jsoup.connect(url).get();
        Elements titles = doc.select("td.title a");

        List<String> urls = new ArrayList<>();

        for (Element title : titles) {
            urls.add(title.attr("href"));
        }
        return urls;
    }

    private static OfficialCalendarCrawlingResponse parseBoardDetail(int codyMenuSeq,
                                                          int boardId,
                                                          int page,
                                                          int boardSeq,
                                                          String url) throws IOException {
        if (url == null) {
            url = String.format("https://www.inu.ac.kr/user/indexSub.do?codyMenuSeq=%d&siteId=dorm&dum=dum&boardId=%d&page=%d&command=view&boardSeq=%d&categoryDepth=0007", codyMenuSeq, boardId, page, boardSeq);
        } else {
            url = "https://www.inu.ac.kr/user/" + url;
        }

        Document doc = Jsoup.connect(url).get();
        Element contentParent = doc.selectFirst("dl.viewdata");
        String title = contentParent.selectFirst("dt").text();
//        Element content = contentParent.selectFirst("dd.contents");
        Element file = contentParent.selectFirst("dd.file a");
        String fileUrl = (file == null) ? null : "https://inu.ac.kr" + file.attr("href");

        return new OfficialCalendarCrawlingResponse(title, url, fileUrl);
    }
}
