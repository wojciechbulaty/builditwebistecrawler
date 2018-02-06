package com.wbsoftwareconsultancy;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class Page {
    private final String url;
    private final List<Page> subPages;
    private final List<String> externalDomainLinks;

    public Page(String url, List<Page> subPages, List<String> externalDomainLinks) {
        this.url = url;
        this.subPages = subPages;
        this.externalDomainLinks = externalDomainLinks;
    }

    public String asString() {
        return asString(0);
    }

    private String asString(int indent) {
        return format("%sPage %s\n" +
                "%sExternal domain links: %s\n" +
                        "%s",
                spaces(indent),
                url,
                spaces(indent),
                toString(externalDomainLinks),
                asString(indent, subPages));
    }

    private String toString(List<String> externalDomainLinks) {
        return externalDomainLinks.isEmpty() ? "none" : externalDomainLinks.stream().collect(Collectors.joining(", "));
    }

    private String asString(int indent, List<Page> subPages) {
        return subPages.stream()
                .map((p) -> p.asString(indent + 1))
                .collect(joining("\n"));
    }

    private String spaces(int indent) {
        return new String(new char[indent * 4]).replace("\0", " ");
    }
}
