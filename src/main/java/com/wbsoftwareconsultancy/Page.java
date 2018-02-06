package com.wbsoftwareconsultancy;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class Page {
    private final String url;
    private final List<Page> subPages;
//    private String css;

    public Page(String url, List<Page> subPages) {
        this.url = url;
        this.subPages = subPages;
    }

    public String asString() {
        return asString(0);
    }

    private String asString(int indent) {
        return format("%sPage %s\n" +
                        "%sStatic content: none\n" +
                        "%s",
                spaces(indent),
                url,
                spaces(indent),
                asString(indent, subPages));
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
