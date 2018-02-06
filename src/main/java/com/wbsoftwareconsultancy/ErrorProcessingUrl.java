package com.wbsoftwareconsultancy;

import java.io.IOException;

public class ErrorProcessingUrl extends CrawlElement {
    private final String url;
    private final IOException e;

    public ErrorProcessingUrl(String url, IOException e) {
        this.url = url;
        this.e = e;
    }

    @Override
    public String asString(int indent) {
        return padding(indent) + "Error processing url " + url + " " + e.toString();
    }
}
