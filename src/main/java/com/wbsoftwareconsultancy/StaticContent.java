package com.wbsoftwareconsultancy;

public class StaticContent extends CrawlElement {
    private final String url;

    public StaticContent(String url) {
        this.url = url;
    }

    @Override
    public String asString(int indent) {
        return padding(indent) + "Static file " + url;
    }
}
