package com.wbsoftwareconsultancy;

import static java.lang.String.format;

public class SiteMap {
    private String url;

    public SiteMap(String url) {
        this.url = url;
    }

    public String asString() {
        return format("Page: %s" +
                "    Internal links: none\n" +
                "    External links: none\n" +
                "    Static content links: none", url);
    }
}
