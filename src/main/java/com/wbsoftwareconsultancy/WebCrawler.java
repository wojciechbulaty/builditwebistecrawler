package com.wbsoftwareconsultancy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class WebCrawler {
    public Page crawl(String rootUrl) throws IOException {
        if (!rootUrl.startsWith("http")) {
            throw new IllegalArgumentException("The url should start with http");
        }
        return crawlPage(rootUrl);
    }

    private Page crawlPage(String crawlUrl) throws IOException {
        Elements links = Jsoup.connect(crawlUrl)
                .get()
                .select("a");
        List<Page> subPages = links.stream()
                .map(WebCrawler::hrefAttribute)
                .filter(WebCrawler::isRelativeLink)
                .map(relativeUrl -> crawlUrl + "/" + relativeUrl)
                .map(this::crawlPageOrHandleException)
                .collect(Collectors.toList());
        return new Page(crawlUrl, subPages);
    }

    public Page crawlPageOrHandleException(String url) {
        try {
            return crawlPage(url);
        } catch (IOException e) {
            // TODO implement sad-path
            e.printStackTrace();
            return null;
        }
    }

    public static String hrefAttribute(Element element) {
        return element.attr("href");
    }

    public static boolean isRelativeLink(String url) {
        return !url.startsWith("http");
    }
}
