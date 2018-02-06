package com.wbsoftwareconsultancy;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class Page extends CrawlElement {
    private final String url;
    private final List<CrawlElement> subPages;
    private final List<String> externalDomainLinks;

    public Page(String url, List<CrawlElement> subPages, List<String> externalDomainLinks) {
        this.url = url;
        this.subPages = subPages;
        this.externalDomainLinks = externalDomainLinks;
    }

    public String asString(int indent) {
        return format("%sPage %s\n" +
                "%sExternal domain links: %s\n" +
                        "%s",
                padding(indent),
                url,
                padding(indent),
                toString(externalDomainLinks),
                asString(indent, subPages));
    }

    private String toString(List<String> externalDomainLinks) {
        return externalDomainLinks.isEmpty() ? "none" : externalDomainLinks.stream().collect(Collectors.joining(", "));
    }

    private String asString(int indent, List<CrawlElement> subPages) {
        return subPages.stream()
                .map((p) -> p.asString(indent + 1))
                .collect(joining("\n"));
    }
}
