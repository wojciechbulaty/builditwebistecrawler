package com.wbsoftwareconsultancy;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.synchronizedSet;
import static java.util.stream.Collectors.toList;

public class WebCrawler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawler.class);
    private static final WebElement ALREADY_SEEN = null;

    public WebElement crawl(String rootUrl) throws IOException {
        if (!rootUrl.startsWith("http")) {
            throw new IllegalArgumentException("The url should start with http");
        }
        return crawlPage(rootUrl, rootUrl, synchronizedSet(new HashSet<>()));
    }

    private WebElement crawlPage(String crawlUrl, String rootUrl, Set<String> alreadyCrawledUrls) throws IOException {
        if (alreadyCrawledUrls.contains(crawlUrl)) {
            return ALREADY_SEEN;
        }
        alreadyCrawledUrls.add(crawlUrl);

        LOGGER.info("Crawling " + crawlUrl);

        Connection connect = Jsoup.connect(crawlUrl);
        String contentType = connect.execute().contentType();
        if (contentType == null || !contentType.contains("text/html")) {
            return new StaticContent(crawlUrl);
        }
        Elements links = connect.get().select("a");

        List<WebElement> subPages = links.parallelStream()
                .map(element -> element.attr("href"))
                .filter(url -> !url.startsWith("#"))
                .filter(url -> url.startsWith(rootUrl) || !url.startsWith("http"))
                .map(url -> url.startsWith(rootUrl) ? url : crawlUrl + "/" + url)
                .map(url -> crawlPageOrHandleException(url, rootUrl, alreadyCrawledUrls))
                .filter(webElement -> webElement != ALREADY_SEEN)
                .collect(toList());

        List<String> externalDomainLinks = links.stream()
                .map(element -> element.attr("href"))
                .filter(url -> !url.startsWith(rootUrl) && url.startsWith("http"))
                .collect(toList());

        LOGGER.info("Finished " + crawlUrl);

        return new Page(crawlUrl, subPages, externalDomainLinks);
    }

    public WebElement crawlPageOrHandleException(String url, String rootUrl, Set<String> alreadyCrawledUrls) {
        try {
            return crawlPage(url, rootUrl, alreadyCrawledUrls);
        } catch (IOException e) {
            // TODO implement sad-path
            e.printStackTrace();
            return new ErrorProcessingUrl(url, e);
        }
    }

    public static void main(String[] args) throws IOException {
        WebElement crawl = new WebCrawler().crawl(args[0]);
        System.out.println("=============================================================");
        System.out.println(crawl.asString());
    }
}
