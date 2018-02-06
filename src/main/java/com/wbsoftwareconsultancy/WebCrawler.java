package com.wbsoftwareconsultancy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebCrawler {
    public SiteMap crawl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
//        Elements newsHeadlines = doc.select("#mp-itn b a");
//        for (Element headline : newsHeadlines) {
//            headline.attr("title"), headline.absUrl("href");
//        }
        return new SiteMap(url);
    }
}
