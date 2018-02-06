package com.wbsoftwareconsultancy;

import java.io.IOException;

public class ErrorProcessingUrl implements WebElement {
    private final String url;
    private final IOException e;

    public ErrorProcessingUrl(String url, IOException e) {
        this.url = url;
        this.e = e;
    }

    @Override
    public String asString() {
        return asString(0);
    }

    @Override
    public String asString(int indent) {
        return "Error processing url " + url + " " + e.toString();
    }
}
