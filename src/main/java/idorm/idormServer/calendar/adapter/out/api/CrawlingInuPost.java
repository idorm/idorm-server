package idorm.idormServer.calendar.adapter.out.api;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.calendar.adapter.out.exception.CrawlingServerError;
import idorm.idormServer.calendar.application.port.out.FindInuPost;
import idorm.idormServer.calendar.application.port.out.LoadOfficialCalendarPort;
import idorm.idormServer.calendar.application.port.out.SaveOfficialCalendarPort;
import idorm.idormServer.calendar.entity.OfficialCalendar;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CrawlingInuPost implements FindInuPost {

	private final LoadOfficialCalendarPort loadOfficialCalendarPort;
	private final SaveOfficialCalendarPort saveOfficialCalendarPort;

	@Transactional
	@Override
	public List<OfficialCalendar> findNewPosts() {
		List<OfficialCalendar> responses = new ArrayList<>();

		String url = "https://bioeng.inu.ac.kr/dorm/6528/subview.do";
		try {
			Document doc = Jsoup.connect(url).get();
			Elements postElements = doc.select("table.board-table tbody tr");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
				if (loadOfficialCalendarPort.findByInuPostId(postId))
					continue;

				String postTitle = postElement.select("td.td-subject a strong").text();

				OfficialCalendar officialCalendar = new OfficialCalendar(postId, postTitle, postUrl, date);
				saveOfficialCalendarPort.save(officialCalendar);
				responses.add(officialCalendar);
				cnt++;
			}
			return responses;
		} catch (IOException e) {
			throw new CrawlingServerError();
		}
	}

	private String parseInuPostId(String url) {
		return url
			.replace("https://bioeng.inu.ac.kr//bbs/dorm/", "")
			.replace("/artclView.do", "")
			.split("/")[1];
	}
}
