package com.wbsoftwareconsultancy;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
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
    private static final CrawlElement ALREADY_SEEN = null;

    public static void main(String[] args) throws IOException {
        CrawlElement crawl = new WebCrawler().crawl(args[0]);
        System.out.println("=============================================================");
        System.out.println(crawl.asString());
    }

    public CrawlElement crawl(String rootUrl) throws IOException {
        if (!rootUrl.startsWith("http")) {
            throw new IllegalArgumentException("The url should start with http");
        }
        return crawlPage(rootUrl, rootUrl, synchronizedSet(new HashSet<>()));
    }

    private CrawlElement crawlPage(String crawlUrl, String rootDomainUrl, Set<String> alreadyCrawledUrls) throws IOException {
        if (alreadyCrawledUrls.contains(crawlUrl)) {
            return ALREADY_SEEN;
        }
        alreadyCrawledUrls.add(crawlUrl);

        LOGGER.info("Crawling " + crawlUrl);

        Connection connect = Jsoup.connect(crawlUrl);
        String contentType = connect.execute().contentType();
        if (isStaticContent(contentType)) {
            return new StaticContent(crawlUrl);
        }
        Elements links = connect.get().select("a");

        List<CrawlElement> subPages = links.parallelStream()
                .map(WebCrawler::elementHref)
                .filter(WebCrawler::nonFragmentUrl)
                .filter(url -> isSameDomain(url, rootDomainUrl))
                .map(url -> appendDomainIfNeeded(crawlUrl, rootDomainUrl, url))
                .map(url -> crawlPageOrHandleException(url, rootDomainUrl, alreadyCrawledUrls))
                .filter(WebCrawler::alreadySeen)
                .collect(toList());

        List<String> externalDomainLinks = links.stream()
                .map(WebCrawler::elementHref)
                .filter(url -> !isSameDomain(url, rootDomainUrl))
                .collect(toList());

        LOGGER.info("Finished " + crawlUrl);

        return new Page(crawlUrl, subPages, externalDomainLinks);
    }

    private boolean isStaticContent(String contentType) {
        return contentType == null || !contentType.contains("text/html");
    }

    private String appendDomainIfNeeded(String crawlUrl, String rootUrl, String url) {
        return url.startsWith(rootUrl) ? url : crawlUrl + "/" + url;
    }

    private boolean isSameDomain(String url, String rootUrl) {
        return url.startsWith(rootUrl) || !url.startsWith("http");
    }

    public CrawlElement crawlPageOrHandleException(String url, String rootUrl, Set<String> alreadyCrawledUrls) {
        try {
            return crawlPage(url, rootUrl, alreadyCrawledUrls);
        } catch (IOException e) {
            return new ErrorProcessingUrl(url, e);
        }
    }

    private static String elementHref(Element element) {
        return element.attr("href");
    }

    private static boolean nonFragmentUrl(String url) {
        return !url.startsWith("#");
    }

    private static boolean alreadySeen(CrawlElement webElement) {
        return webElement != ALREADY_SEEN;
    }
}
