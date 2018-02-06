package com.wbsoftwareconsultancy;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class WebCrawler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawler.class);

    public WebElement crawl(String rootUrl) throws IOException {
        if (!rootUrl.startsWith("http")) {
            throw new IllegalArgumentException("The url should start with http");
        }
        return crawlPage(rootUrl, rootUrl);
    }

    private WebElement crawlPage(String crawlUrl, String rootUrl) throws IOException {
        LOGGER.info("Crawling " + crawlUrl);

        Connection connect = Jsoup.connect(crawlUrl);
        String contentType = connect.execute().contentType();
        if (contentType == null || !contentType.contains("text/html")) {
            return new StaticContent(crawlUrl);
        }
        Elements links = connect.get().select("a");

        List<WebElement> subPages = links.stream()
                .map(WebCrawler::hrefAttribute)
                .filter(url -> !url.startsWith("#"))
                .filter(url -> url.startsWith(rootUrl) || !url.startsWith("http"))
                .map(url -> url.startsWith(rootUrl) ? url : crawlUrl + "/" + url)
                .map(url -> crawlPageOrHandleException(url, rootUrl))
                .collect(toList());

        List<String> externalDomainLinks = links.stream()
                .map(WebCrawler::hrefAttribute)
                .filter(url -> !url.startsWith(rootUrl) && url.startsWith("http"))
                .collect(toList());

        LOGGER.info("Finished " + crawlUrl);

        return new Page(crawlUrl, subPages, externalDomainLinks);
    }

    public WebElement crawlPageOrHandleException(String url, String rootUrl) {
        try {
            return crawlPage(url, rootUrl);
        } catch (IOException e) {
            // TODO implement sad-path
            e.printStackTrace();
            return new ErrorProcessingUrl(url, e);
        }
    }

    public static String hrefAttribute(Element element) {
        return element.attr("href");
    }

    public static boolean isExternalLink(String url) {
        return url.startsWith("http");
    }
}
